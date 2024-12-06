package com.balugaq.rsceditor.implementation.items;

import com.balugaq.rsceditor.implementation.RSCEditor;
import com.balugaq.rsceditor.implementation.items.machines.ArmorEditor;
import com.balugaq.rsceditor.implementation.items.machines.CapacitorEditor;
import com.balugaq.rsceditor.implementation.items.machines.FoodEditor;
import com.balugaq.rsceditor.implementation.items.machines.GEOResourceEditor;
import com.balugaq.rsceditor.implementation.items.machines.GeneratorEditor;
import com.balugaq.rsceditor.implementation.items.machines.ItemEditor;
import com.balugaq.rsceditor.implementation.items.machines.ItemGroupEditor;
import com.balugaq.rsceditor.implementation.items.machines.LinkedRecipeMachineEditor;
import com.balugaq.rsceditor.implementation.items.machines.MachineEditor;
import com.balugaq.rsceditor.implementation.items.machines.MaterialGeneratorEditor;
import com.balugaq.rsceditor.implementation.items.machines.MobDropEditor;
import com.balugaq.rsceditor.implementation.items.machines.MultiBlockEditor;
import com.balugaq.rsceditor.implementation.items.machines.RecipeMachineEditor;
import com.balugaq.rsceditor.implementation.items.machines.RecipeTypeEditor;
import com.balugaq.rsceditor.implementation.items.machines.ResearchEditor;
import com.balugaq.rsceditor.implementation.items.machines.SimpleMachineEditor;
import com.balugaq.rsceditor.implementation.items.machines.SolarGeneratorEditor;
import com.balugaq.rsceditor.implementation.items.machines.TemplateRecipeMachineEditor;
import com.balugaq.rsceditor.implementation.items.machines.WorkbenchEditor;
import com.balugaq.rsceditor.implementation.items.machines.builder.LinkedMachineRecipeBuilder;
import com.balugaq.rsceditor.implementation.items.machines.builder.MachineRecipeBuilder;
import com.balugaq.rsceditor.implementation.items.machines.builder.RegisterBuilder;
import com.balugaq.rsceditor.implementation.items.machines.container.ArmorPieceContainer;
import com.balugaq.rsceditor.implementation.items.machines.container.CustomRainbowContainer;
import com.balugaq.rsceditor.implementation.items.machines.container.ItemFlowContainer;
import com.balugaq.rsceditor.implementation.items.machines.container.MenuContainer;
import com.balugaq.rsceditor.implementation.items.machines.container.SupplyContainer;
import com.balugaq.rsceditor.utils.SlimefunItemUtil;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class MachineSetup {
    public static void register() {
        // containers
        ArmorPieceContainer armorPieceContainer = new ArmorPieceContainer(
                new SlimefunItemStack(
                        "RSC_EDITOR_ARMOR_PIECE_CONTAINER",
                        new CustomItemStack(
                                Material.IRON_BLOCK,
                                "&b装备部分容器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(armorPieceContainer);

        CustomRainbowContainer customRainbowContainer = new CustomRainbowContainer(
                new SlimefunItemStack(
                        "RSC_EDITOR_CUSTOM_RAINBOW_CONTAINER",
                        new CustomItemStack(
                                Material.ORANGE_WOOL,
                                "&b自定义彩虹物品容器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(customRainbowContainer);

        SupplyContainer supplyContainer = new SupplyContainer(
                new SlimefunItemStack(
                        "RSC_EDITOR_SUPPLY_CONTAINER",
                        new CustomItemStack(
                                Material.MOSS_BLOCK,
                                "&bGEO供应容器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(supplyContainer);

        ItemFlowContainer itemFlowContainer = new ItemFlowContainer(
                new SlimefunItemStack(
                        "RSC_EDITOR_ITEM_FLOW_CONTAINER",
                        new CustomItemStack(
                                Material.LODESTONE,
                                "&b物品流容器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(itemFlowContainer);

        MenuContainer menuContainer = new MenuContainer(
                new SlimefunItemStack(
                        "RSC_EDITOR_MENU_CONTAINER",
                        new CustomItemStack(
                                Material.BOOKSHELF,
                                "&b菜单容器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(menuContainer);

        // builders
        MachineRecipeBuilder machineRecipeBuilder = new MachineRecipeBuilder(
                new SlimefunItemStack(
                        "RSC_EDITOR_MACHINE_RECIPE_BUILDER",
                        new CustomItemStack(
                                Material.SMITHING_TABLE,
                                "&b机器配方配置器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(machineRecipeBuilder);

        LinkedMachineRecipeBuilder linkedMachineRecipeBuilder = new LinkedMachineRecipeBuilder(
                new SlimefunItemStack(
                        "RSC_EDITOR_LINKED_MACHINE_RECIPE_BUILDER",
                        new CustomItemStack(
                                Material.LOOM,
                                "&b强配方机器配方配置器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(linkedMachineRecipeBuilder);

        RegisterBuilder registerBuilder = new RegisterBuilder(
                new SlimefunItemStack(
                        "RSC_EDITOR_REGISTER_BUILDER",
                        new CustomItemStack(
                                Material.REDSTONE_LAMP,
                                "&b注册选项配置器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(registerBuilder);

        // editors
        ItemGroupEditor itemGroupEditor = new ItemGroupEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_ITEM_GROUP_EDITOR",
                        new CustomItemStack(
                                Material.FLETCHING_TABLE,
                                "&b物品组构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(itemGroupEditor);

        RecipeTypeEditor recipeTypeEditor = new RecipeTypeEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_RECIPE_TYPE_EDITOR",
                        new CustomItemStack(
                                Material.CARTOGRAPHY_TABLE,
                                "&b配方类型构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(recipeTypeEditor);

        GEOResourceEditor geoResourceEditor = new GEOResourceEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_GEO_RESOURCE_EDITOR",
                        new CustomItemStack(
                                Material.GRASS_BLOCK,
                                "&b自然资源构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(geoResourceEditor);

        MobDropEditor mobDropEditor = new MobDropEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_MOB_DROP_EDITOR",
                        new CustomItemStack(
                                Material.SPAWNER,
                                "&b生物掉落构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(mobDropEditor);

        ItemEditor itemEditor = new ItemEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_ITEM_EDITOR",
                        new CustomItemStack(
                                Material.SLIME_BLOCK,
                                "&b物品构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(itemEditor);

        ArmorEditor armorEditor = new ArmorEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_ARMOR_EDITOR",
                        new CustomItemStack(
                                Material.GOLD_BLOCK,
                                "&b装备构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(armorEditor);

        CapacitorEditor capacitorEditor = new CapacitorEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_CAPACITOR_EDITOR",
                        new CustomItemStack(
                                Material.REDSTONE_BLOCK,
                                "&b电容构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(capacitorEditor);

        FoodEditor foodEditor = new FoodEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_FOOD_EDITOR",
                        new CustomItemStack(
                                Material.HAY_BLOCK,
                                "&b食物构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(foodEditor);

        MachineEditor machineEditor = new MachineEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_MACHINE_EDITOR",
                        new CustomItemStack(
                                Material.BLAST_FURNACE,
                                "&b机器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(machineEditor);

        GeneratorEditor generatorEditor = new GeneratorEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_GENERATOR_EDITOR",
                        new CustomItemStack(
                                Material.GLOWSTONE,
                                "&b发电机构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(generatorEditor);

        SolarGeneratorEditor solarGeneratorEditor = new SolarGeneratorEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_SOLAR_GENERATOR_EDITOR",
                        new CustomItemStack(
                                Material.SHROOMLIGHT,
                                "&b太阳能发电机构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(solarGeneratorEditor);

        MaterialGeneratorEditor materialGeneratorEditor = new MaterialGeneratorEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_MATERIAL_GENERATOR_EDITOR",
                        new CustomItemStack(
                                Material.DIAMOND_BLOCK,
                                "&b材料生成器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(materialGeneratorEditor);

        RecipeMachineEditor recipeMachineEditor = new RecipeMachineEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_RECIPE_MACHINE_EDITOR",
                        new CustomItemStack(
                                Material.FURNACE,
                                "&b配方机器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(recipeMachineEditor);

        SimpleMachineEditor simpleMachineEditor = new SimpleMachineEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_SIMPLE_MACHINE_EDITOR",
                        new CustomItemStack(
                                Material.SMOKER,
                                "&b简单机器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(simpleMachineEditor);

        MultiBlockEditor multiBlockEditor = new MultiBlockEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_MULTI_BLOCK_EDITOR",
                        new CustomItemStack(
                                Material.BRICKS,
                                "&b多方块构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(multiBlockEditor);

        TemplateRecipeMachineEditor templateRecipeMachineEditor = new TemplateRecipeMachineEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_TEMPLATE_RECIPE_MACHINE_EDITOR",
                        new CustomItemStack(
                                Material.EMERALD_BLOCK,
                                "&b模板配方机器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(templateRecipeMachineEditor);

        LinkedRecipeMachineEditor linkedRecipeMachineEditor = new LinkedRecipeMachineEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_LINKED_RECIPE_MACHINE_EDITOR",
                        new CustomItemStack(
                                Material.SEA_LANTERN,
                                "&b强配方机器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(linkedRecipeMachineEditor);

        WorkbenchEditor workbenchEditor = new WorkbenchEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_WORKBENCH_EDITOR",
                        new CustomItemStack(
                                Material.CRAFTING_TABLE,
                                "&b工作台机器构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(workbenchEditor);

        ResearchEditor researchEditor = new ResearchEditor(
                new SlimefunItemStack(
                        "RSC_EDITOR_RESEARCH_EDITOR",
                        new CustomItemStack(
                                Material.ENCHANTING_TABLE,
                                "&b研究构造器"
                        )
                )
        );
        SlimefunItemUtil.registerItem(researchEditor);
    }
}
