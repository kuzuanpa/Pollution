package keqing.pollution.common.metatileentity.multiblock;

import gregicality.multiblocks.api.GCYMValues;
import gregicality.multiblocks.api.metatileentity.GCYMMultiblockAbility;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiFluidHatch;
import gregtech.common.metatileentities.multi.multiblockpart.appeng.MetaTileEntityMEOutputHatch;
import keqing.pollution.api.metatileentity.POManaMultiblock;
import keqing.pollution.api.metatileentity.POMultiblockAbility;
import keqing.pollution.client.textures.POTextures;
import keqing.pollution.common.block.PollutionMetaBlocks;
import keqing.pollution.common.block.metablocks.POBotBlock;
import keqing.pollution.common.block.metablocks.POGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.List;

import static gregtech.api.util.RelativeDirection.*;
import static keqing.pollution.common.block.metablocks.POBotBlock.BotBlockType.TERRA_WATERTIGHT_CASING;

public class MetaTileEntityBotDistillery extends POManaMultiblock {
    //工作配方
    public MetaTileEntityBotDistillery(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{RecipeMaps.DISTILLATION_RECIPES, RecipeMaps.DISTILLERY_RECIPES});
    }
    //方块注册
    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity metaTileEntityHolder) {
        return new MetaTileEntityBotDistillery(this.metaTileEntityId);
    }
    //多方块摆放
    @Override
    protected  BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start(RIGHT, FRONT, UP)
                .aisle("___XXX___","___XXX___","__XXXXX__","XXXXXXXXX","XXXXXXXXX","XXXXXXXXX","__XXXXX__","___XXX___","___XXX___")
                .aisle("_________","___XSX___","__XXXXX__","_XXXXXXX_","_XXXZXXX_","_XXXXXXX_","__XXXXX__","___XXX___","_________")
                .aisle("_________","____X____","___XXX___","__XXXXX__","_XXXZXXX_","__XXXXX__","___XXX___","____X____","_________")
                .aisle("_________","_________","___Y_Y___","__Y___Y__","____Z____","__Y___Y__","___Y_Y___","_________","_________").setRepeatable(1, 12)
                .aisle("_________","_________","_________","_________","____Z____","_________","_________","_________","_________")
                .aisle("_________","_________","_________","_________","____Z____","_________","_________","_________","_________")

                .where('S', selfPredicate())
                .where('X', states(getCasingState()).setMinGlobalLimited(40)
                        .or(autoAbilities(false,false,false,false,
                                false,false,false))
//                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH).setExactLimit(1).setPreviewCount(1))
//                        .or(abilities(POMultiblockAbility.MANA_HATCH).setMaxGlobalLimited(1).setPreviewCount(1))
//                        .or(abilities(GCYMMultiblockAbility.PARALLEL_HATCH).setMaxGlobalLimited(1).setPreviewCount(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY).setMaxGlobalLimited(2).setMinGlobalLimited(1).setPreviewCount(2))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS).setMaxGlobalLimited(1).setMinGlobalLimited(1).setPreviewCount(1))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS).setMaxGlobalLimited(1).setMinGlobalLimited(1).setPreviewCount(1))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMaxGlobalLimited(4).setMinGlobalLimited(1).setPreviewCount(1))

                )
                .where('Y', states(getCasingState2())
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS).setMaxLayerLimited(1).setMinLayerLimited(1).setPreviewCount(1))
                )
                .where('Z', states(getCasingState3()))
                .where('_', any())
                .build();
    }

    //设置外壳方块
    private static IBlockState getCasingState() {
        return PollutionMetaBlocks.BOT_BLOCK.getState(POBotBlock.BotBlockType.TERRA_WATERTIGHT_CASING);
    }

    private static IBlockState getCasingState2() {
        return PollutionMetaBlocks.GLASS.getState(POGlass.MagicBlockType.CAMINATED_GLASS);
    }
    private static IBlockState getCasingState3() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS);
    }
    //设置主方块和功能仓室纹理
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return POTextures.TERRA_WATERTIGHT_CASING;
    }
    //设置主方块机器纹理
    @Override
    protected  OrientedOverlayRenderer getFrontOverlay() {
        return Textures.HPCA_OVERLAY;
    }
    //总线隔离
    @Override
    public boolean canBeDistinct() {
        return true;
    }
    //工具提示
    @Override
    public void addInformation(ItemStack stack,  World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("pollution.machine.bot_distillery", 1));
    }

}