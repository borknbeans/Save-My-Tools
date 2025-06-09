package borknbeans.savemytools.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import borknbeans.savemytools.SaveMyToolsClient;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    private static final Text MESSAGE = Text.of("This tool is going to break!");

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void attackEntity(PlayerEntity player, Entity target, CallbackInfo info) {
        if (player != null && target != null) {
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof ToolItem && stack.getDamage() >= stack.getMaxDamage() - 1 && !SaveMyToolsClient.ignoreWarningKeyBind.isPressed()) {
                info.cancel();
                player.sendMessage(Text.of(MESSAGE), true);
            }
        }
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null && client.player != null) {
            ItemStack stack = client.player.getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof ToolItem && stack.getDamage() >= stack.getMaxDamage() - 1 && !SaveMyToolsClient.ignoreWarningKeyBind.isPressed()) {
                info.setReturnValue(false);
                client.player.sendMessage(Text.of(MESSAGE), true);
            }
        }
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> info) {
        if (player != null) {
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            
            if (stack.getItem() instanceof ToolItem && stack.getDamage() >= stack.getMaxDamage() - 1 && !SaveMyToolsClient.ignoreWarningKeyBind.isPressed()) {
                BlockPos blockPos = hitResult.getBlockPos();
                
                Block block = player.getWorld().getBlockState(blockPos).getBlock();

                
                if (stack.getItem() instanceof AxeItem && (block.asItem().getName().getString().toLowerCase().contains("log") || VanillaStrippableBlocks.contains(block))) {
                    info.setReturnValue(ActionResult.FAIL);
                    player.sendMessage(Text.of(MESSAGE), true);
                }

                if (stack.getItem() instanceof ShovelItem && VanillaPathableBlocks.contains(block)) {
                    info.setReturnValue(ActionResult.FAIL);
                    player.sendMessage(Text.of(MESSAGE), true);
                }

                if (stack.getItem() instanceof HoeItem && VanillaTillableBlocks.contains(block)) {
                    info.setReturnValue(ActionResult.FAIL);
                    player.sendMessage(Text.of(MESSAGE), true);
                }
            }
        }
    }
    
    private static Set<Block> VanillaStrippableBlocks = Set.of(
        Blocks.OAK_WOOD, Blocks.OAK_LOG,
        Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_LOG,
        Blocks.ACACIA_WOOD, Blocks.ACACIA_LOG,
        Blocks.CHERRY_WOOD, Blocks.CHERRY_LOG,
        Blocks.BIRCH_WOOD, Blocks.BIRCH_LOG,
        Blocks.JUNGLE_WOOD, Blocks.JUNGLE_LOG,
        Blocks.SPRUCE_WOOD, Blocks.SPRUCE_LOG,
        Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE,
        Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE,
        Blocks.MANGROVE_WOOD, Blocks.MANGROVE_LOG,
        Blocks.BAMBOO_BLOCK
    );

    private static Set<Block> VanillaPathableBlocks = Set.of(
        Blocks.GRASS_BLOCK,
        Blocks.DIRT,
        Blocks.PODZOL,
        Blocks.COARSE_DIRT,
        Blocks.MYCELIUM,
        Blocks.ROOTED_DIRT
    );

    private static Set<Block> VanillaTillableBlocks = Set.of(
        Blocks.GRASS_BLOCK,
        Blocks.DIRT_PATH,
        Blocks.DIRT,
        Blocks.COARSE_DIRT,
        Blocks.ROOTED_DIRT
    );
}
