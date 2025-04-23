package com.balugaq.rsceditor.implementation.groups;

import com.balugaq.rsceditor.implementation.RSCEditor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupSetup {
    public static void register() {
        RSCEItemGroups.RSC_EDITOR_GROUP.register(RSCEditor.getInstance());
        RSCEItemGroups.AUTHOR_GROUP.register(RSCEditor.getInstance());
        RSCEItemGroups.MACHINE_GROUP.register(RSCEditor.getInstance());
        RSCEItemGroups.ITEM_GROUP_GROUP.register(RSCEditor.getInstance());
        RSCEItemGroups.RECIPE_TYPE_GROUP.register(RSCEditor.getInstance());
        RSCEItemGroups.BIOME_GROUP.register(RSCEditor.getInstance());
        RSCEItemGroups.TYPE_GROUP.register(RSCEditor.getInstance());
    }
}
