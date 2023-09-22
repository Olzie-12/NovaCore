call mvn clean package

if exist %cd%\NovaCore-Spigot.jar del %cd%\NovaCore-Spigot.jar
if exist %cd%\NovaCore-Bungeecord.jar del %cd%\NovaCore-Bungeecord.jar
if exist %cd%\NovaCore-GameEngine.jar del %cd%\NovaCore-GameEngine.jar
if exist %cd%\NovaUtils.jar del %cd%\NovaUtils.jar

copy %cd%\NovaCore-Spigot\target\NovaCore-Spigot-2.0.0-SNAPSHOT.jar %cd%\NovaCore-Spigot.jar
copy %cd%\NovaCore-Bungeecord\target\NovaCore-Bungeecord-2.0.0-SNAPSHOT.jar %cd%\NovaCore-Bungeecord.jar
copy %cd%\NovaCore-GameEngine\target\NovaCore-GameEngine-2.0.0-SNAPSHOT.jar %cd%\NovaCore-GameEngine.jar
copy %cd%\NovaUtils\target\NovaUtils-2.0.0-SNAPSHOT.jar %cd%\NovaUtils.jar
copy %cd%\NovaCore-GameMapDesignToolkit\target\GameMapDesignToolkit-2.0.0-SNAPSHOT.jar %cd%\GameMapDesignToolkit.jar

timeout /T 5