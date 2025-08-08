package com.example.staticvault;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class StaticVaultRenderer implements BlockEntityRenderer<BlockEntity> {

    private final ItemRenderer itemRenderer;

    public StaticVaultRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(BlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (!(entity instanceof VaultBlockEntity vault)) return;

        ItemStack stack = getStoredItem(vault);
        if (stack.isEmpty()) return;

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(0.75f, 0.75f, 0.75f);

        itemRenderer.renderItem(
                stack,
                net.minecraft.client.render.model.json.ModelTransformationMode.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
    }

    private ItemStack getStoredItem(VaultBlockEntity vault) {
        try {
            var clientData = vault.getClientData();
            if (clientData == null) return ItemStack.EMPTY;

            var config = clientData.config();
            if (config == null) return ItemStack.EMPTY;

            var keyItem = config.keyItem();
            if (keyItem == null) return ItemStack.EMPTY;

            return keyItem;
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }
}
