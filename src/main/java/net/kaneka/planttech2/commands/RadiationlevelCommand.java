package net.kaneka.planttech2.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kaneka.planttech2.entities.capabilities.player.RadiationEffect;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RadiationlevelCommand
{
    public RadiationlevelCommand() {}
    public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> register(CommandDispatcher<CommandSource> dispatcher)
    {
        return Commands.literal("radiationlevel")
                .then(Commands.literal("add")
                .then(Commands.argument("amount", FloatArgumentType.floatArg(0, 2))
                .executes((context) -> add(context, FloatArgumentType.getFloat(context, "amount")))))
                .then(Commands.literal("set")
                .then(Commands.argument("value", FloatArgumentType.floatArg(0, 2))
                .executes((context) -> set(context, FloatArgumentType.getFloat(context, "value")))))
                .then(Commands.literal("clear")
                .executes((context) -> set(context, 0)))
                .then(Commands.literal("full")
                .executes((context) -> set(context, 2)));
    }

    private static int add(CommandContext<CommandSource> context, float amount) throws CommandSyntaxException
    {
        RadiationEffect.getCap(context.getSource().asPlayer()).increaseLevel(amount);
        return (int) amount;
    }

    private static int set(CommandContext<CommandSource> context, float amount) throws CommandSyntaxException
    {
        RadiationEffect.getCap(context.getSource().asPlayer()).setLevel(amount);
        return (int) amount;
    }
}
