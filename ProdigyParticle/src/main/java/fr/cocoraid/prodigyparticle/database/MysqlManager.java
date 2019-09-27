package fr.cocoraid.prodigyparticle.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.cocoraid.prodigyparticle.particle.ParticlesManager;
import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MysqlManager {
    private Map<UUID,String> cache = new HashMap<>();
    private HikariDataSource hikari;
    private ParticlesManager manager;
    public boolean mysqlDisconnected = false;

    public MysqlManager(ParticlesManager manager) {
        this.manager = manager;
    }

    public MysqlManager setup(){
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://"+ "" + ":3306" + "/"+ "");
            config.setUsername("user");
            config.setPassword("password");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCachSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setDriverClassName("com.mysql.jdbc.Driver");
            hikari = new HikariDataSource(config);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL Connect: " + "It's OK ! ProdigyParticle will use MySQL");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MySQL Connect Error: " + "Connection to database has failed !");
            e.printStackTrace();
        }
        createTable();
        return this;
    }

    /**
     * Create table database
     */
    public void createTable(){
        try(Connection connection = hikari.getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Particles(UUID varchar(36), particle varchar(36),PRIMARY KEY(UUID))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //register
    private final String INSERT = "INSERT INTO Particles(uuid,particle) VALUES(?,?) ON DUPLICATE KEY UPDATE uuid=?";

    //set
    private final String UPDATE = "UPDATE Particles SET particle=? WHERE uuid=?";

    //get
    private final String SELECT = "SELECT Particles.particle FROM Particles WHERE uuid=?";


    public Connection startConnection(){
        Connection connection = null;
        if(mysqlDisconnected){
            return connection;
        }
        try {
            connection = hikari.getConnection();
            if(connection == null) {
                mysqlDisconnected = true;
                return connection;
            }
            return connection;

        } catch(SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: MySQL disconnected. ");
            mysqlDisconnected = true;
            e.printStackTrace();
        }
        return null;
    }


    public void saveInAsync(UUID uuid) {
        try(Connection connection = startConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
            preparedStatement.setString(1,uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String particle = resultSet.getString("particle");
                if(!particle.equalsIgnoreCase("null")) {
                    cache.put(uuid, particle);
                }
            }
            resultSet.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public void setParticleBack(UUID uuid) {
        if(!cache.containsKey(uuid)) return;
        String particleName = cache.get(uuid);
        manager.getParticles().stream().filter(pp -> pp.getClass().getSimpleName().equalsIgnoreCase(particleName)).findAny().ifPresent(pp -> {
            pp.toggleParticle(Bukkit.getPlayer(uuid));
        });
    }


    /**
     * Sauvegarder dans la base de donnée
     * @param uuid
     */
     public void saveOutAsync(UUID uuid) {

        try(Connection connection = startConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                PreparedStatement updateStat = connection.prepareStatement(UPDATE);

                if (manager.getEnabledParticles().containsKey(uuid)) {
                    String particle = manager.getEnabledParticles().get(uuid).getClass().getSimpleName();
                    if (particle.equalsIgnoreCase(cache.get(uuid))) {
                        preparedStatement.close();
                        resultSet.close();
                        return;
                    }
                    updateStat.setString(1, particle);
                    updateStat.setString(2, uuid.toString());
                    updateStat.execute();
                    updateStat.close();
                } else {
                    if (cache.containsKey(uuid)) {
                        updateStat.setString(1, "null");
                        updateStat.setString(2, uuid.toString());
                        updateStat.execute();
                        updateStat.close();

                    }
                }
            } else {
                if (manager.getEnabledParticles().containsKey(uuid)) {
                    String particle = manager.getEnabledParticles().get(uuid).getClass().getSimpleName();
                    PreparedStatement insertStat = connection.prepareStatement(INSERT);
                    insertStat.setString(1, uuid.toString());
                    insertStat.setString(2, particle);
                    insertStat.setString(3, uuid.toString());
                    insertStat.execute();
                    insertStat.close();
                }
            }
            cache.remove(uuid);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
