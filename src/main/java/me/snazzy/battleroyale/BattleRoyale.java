package me.snazzy.battleroyale;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BattleRoyale extends Thread implements Listener, CommandExecutor {

    static HashMap<String, Boolean> ReadyPlayers = new HashMap<>();
    static int NumReadyPlayers;
    static int AdventurePlayers = 0;
    static Location spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();
    static Location startLoc = Bukkit.getServer().getWorld("world").getBlockAt(0, 0, 0).getLocation().add(0, 300,0);
    static boolean isGameStarted = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.getPlayer().teleport(spawn);
        }

        if (isGameStarted && event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);

        } else if (!isGameStarted && event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            AdventurePlayers ++;
            System.out.println("Adventure players: " + AdventurePlayers);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            AdventurePlayers --;
            System.out.println("Adventure players: " + AdventurePlayers);
        }
    }

    @EventHandler
    public void GamemodeChange(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode().equals(GameMode.ADVENTURE)) {
            AdventurePlayers ++;
        } else if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            AdventurePlayers --;
        }
        System.out.println("Adventure players: " + AdventurePlayers);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ready")) {

            if (isGameStarted) {
                sender.sendMessage(ChatColor.RED + "You cannot ready up while there is a round in progress.");

            } else if (ReadyPlayers.containsKey(sender.getName())) {
                sender.sendMessage(ChatColor.RED + "You are already ready.");

            } else {
                ReadyPlayers.put(sender.getName(), Boolean.TRUE);
                NumReadyPlayers = ReadyPlayers.size();
                System.out.println("Ready players: " + ReadyPlayers);
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendMessage(ChatColor.GREEN
                            + sender.getName()
                            + " is now ready!\n"
                            + ChatColor.AQUA
                            + NumReadyPlayers
                            + ChatColor.GREEN
                            + " out of "
                            + ChatColor.AQUA
                            + AdventurePlayers
                            + ChatColor.GREEN
                            + " players are ready.");

                    if (AdventurePlayers <= 1) {
                        player.sendMessage(ChatColor.GREEN + "There is only 1 player online so the game isn't starting.");
                    }
                }

                if ((NumReadyPlayers >= AdventurePlayers) && (NumReadyPlayers > 1)) {
                    startGame();
                }
            }
        }

        if (label.equalsIgnoreCase("unready")) {
            if (!ReadyPlayers.containsKey(sender.getName())) {
                sender.sendMessage(ChatColor.RED + "You are already not ready.");
            } else {
                ReadyPlayers.remove(sender.getName());
                System.out.println("Ready players: " + ReadyPlayers);
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendMessage(ChatColor.GREEN
                            + sender.getName()
                            + " is no longer ready.\n"
                            + ChatColor.AQUA
                            + (NumReadyPlayers - 1)
                            + ChatColor.GREEN
                            + " out of "
                            + ChatColor.AQUA
                            + AdventurePlayers
                            + ChatColor.GREEN
                            + " players are ready.");
                }
            }
        }

        if (label.equalsIgnoreCase("force")) {
            if (args[0].equalsIgnoreCase("start") && sender.hasPermission("force.use")) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendMessage(ChatColor.GREEN + "The game has been forced to start");
                }
                startGame();
            } else if (args[0].equalsIgnoreCase("end") && sender.hasPermission("force.use")) {
                endGame(null);
            }
        }

        if (label.equalsIgnoreCase("fix") && sender.hasPermission("fix.use")) {
            AdventurePlayers = 0;
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    AdventurePlayers++;
                }
            }
            sender.sendMessage("Adventure players recalculated. Currently: " + AdventurePlayers);
        }
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Material blockUnderPlayer = event.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType();

        if (!blockUnderPlayer.equals(Material.AIR) && !blockUnderPlayer.equals(Material.VOID_AIR)) {
            try {
                if (event.getPlayer().getInventory().getChestplate().getType().equals(Material.ELYTRA)) {
                    event.getPlayer().getInventory().setChestplate(null);
                }
            }
            catch (NullPointerException e) {

            }
        }
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE) && !isGameStarted) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getTargetBlockExact(6) == null) {return;}
        if (event.getPlayer().getTargetBlockExact(6).getType().equals(Material.CHIPPED_ANVIL) || event.getPlayer().getTargetBlockExact(6).getType().equals(Material.DAMAGED_ANVIL)) {
            event.getPlayer().getTargetBlockExact(6).setType(Material.ANVIL);
        }
    }

    @EventHandler
    public void onBlockBreak(EntityChangeBlockEvent event) {
        if (event.getEntity().getType().equals(EntityType.BOAT)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent event) {
        Player breaker = (Player) event.getRemover();
        if (breaker.getGameMode().equals(GameMode.ADVENTURE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {return;}
        Player attacker = (Player) event.getDamager();
        if (!(event.getEntity() instanceof ItemFrame)) {return;}
        if (attacker.getGameMode().equals(GameMode.ADVENTURE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {return;}
        if (!isGameStarted) {event.setCancelled(true);}

        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fix");
        Player killedPlayer = (Player) event.getEntity();

        if (((killedPlayer.getHealth() - event.getFinalDamage()) <= 0) && !killedPlayer.getGameMode().equals(GameMode.SPECTATOR)) {
            event.setCancelled(true);
            for (ItemStack item : killedPlayer.getInventory().getContents()) {
                if ((item != null) && (!item.getType().equals(Material.ELYTRA))) {
                    killedPlayer.getWorld().dropItemNaturally(killedPlayer.getLocation(), item);
                }
            }
            killedPlayer.setGameMode(GameMode.SPECTATOR);
            killedPlayer.getLocation().getWorld().playSound(killedPlayer.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 1);
            killedPlayer.getInventory().clear();

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + killedPlayer.getName() + " has been eliminated!");
            }

            if (AdventurePlayers == 1) {
                Player winner = null;
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                        winner = player;
                    }
                }
                endGame(winner);
            }
        }
    }

    public static void startGame() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fix");
        int timeLeft = 5;
        while (timeLeft > 0) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage("Starting in " + timeLeft + "...");
            }
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            timeLeft--;
        }
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                player.getInventory().clear();
                player.setExp(0);
                player.setLevel(0);
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                player.teleport(startLoc);
                player.getInventory().setChestplate(elytra);
                player.sendMessage(ChatColor.RED + "The game has started, Good luck!");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "effect give @a instant_health 1 5");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "effect give @a saturation 1 20");
            }
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "worldborder set 50 600");
        ReadyPlayers.clear();
        NumReadyPlayers = 0;
        isGameStarted = true;
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "refill");
    }

    public static void endGame(Player winner) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "worldborder set 800");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (winner == null) {
                player.sendMessage(ChatColor.RED + "The game has been forced to end");
            } else {
                player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + winner.getName() + " has won the game!");
                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10, 1);
            }
        }
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(spawn);
            player.getInventory().clear();
            player.setExp(0);
            player.setLevel(0);

        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "effect give @a instant_health 1 5");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "effect give @a saturation 1 20");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kill @e[type=item]");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kill @e[type=boat]");
        isGameStarted = false;
    }
}
