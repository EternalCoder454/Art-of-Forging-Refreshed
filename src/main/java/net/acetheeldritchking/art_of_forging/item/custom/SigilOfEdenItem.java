package net.acetheeldritchking.art_of_forging.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class SigilOfEdenItem extends Item {
    public SigilOfEdenItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pDisplay,
                                Consumer<Component> pTooltipAdder, TooltipFlag pIsAdvanced) {
        pTooltipAdder.accept(Component.translatable("item.sigil_of_eden.tooltip").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));

        super.appendHoverText(pStack, pContext, pDisplay, pTooltipAdder, pIsAdvanced);
    }
}
