package com.song.nuclear_craft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.song.nuclear_craft.entities.rocket_entities.IncendiaryRocketEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;

public class NuclearCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> nuclearCommand = Commands.literal("nuclear")
                .then(Commands.literal("incendiary")
                        .executes(NuclearCommand::executeIncendiaryAll)
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(NuclearCommand::executeIncendiaryTarget)));
        
        dispatcher.register(nuclearCommand);
    }
    
    private static int executeIncendiaryAll(CommandContext<CommandSourceStack> context) {
        Level world = context.getSource().getLevel();
        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            spawnIncendiaryExplosion(world, player);
        }
        return 1;
    }
    
    private static int executeIncendiaryTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(context, "target");
        Level world = context.getSource().getLevel();
        spawnIncendiaryExplosion(world, player);
        return 1;
    }
    
    private static void spawnIncendiaryExplosion(Level world, Player player) {
        Random random = new Random();
        
        // 在玩家头顶10-15格的10x10范围内随机位置
        double offsetX = (random.nextDouble() - 0.5) * 10;
        double offsetZ = (random.nextDouble() - 0.5) * 10;
        double yOffset = random.nextDouble() * 5 + 10;
        
        double x = player.getX() + offsetX;
        double y = player.getY() + yOffset;
        double z = player.getZ() + offsetZ;
        
        // 直接调用燃烧爆炸方法，不需要实体
        double validY = IncendiaryRocketEntity.getValidY(world, x, y, z);
        IncendiaryRocketEntity.fireExplode(world, x, validY, z);
    }
}