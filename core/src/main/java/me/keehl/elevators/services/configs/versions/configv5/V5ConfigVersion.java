package me.keehl.elevators.services.configs.versions.configv5;

import me.keehl.elevators.Elevators;
import me.keehl.elevators.models.ElevatorRecipeGroup;
import me.keehl.elevators.models.ElevatorType;
import me.keehl.elevators.models.hooks.ProtectionHook;
import me.keehl.elevators.services.ElevatorHookService;
import me.keehl.elevators.services.configs.ConfigVersion;
import me.keehl.elevators.services.configs.versions.configv4_0_2.V4_0_2ConfigRecipe;
import me.keehl.elevators.services.configs.versions.configv4_0_2.V4_0_2ConfigRoot;
import org.bukkit.DyeColor;

import java.util.HashMap;

public class V5ConfigVersion extends ConfigVersion<V4_0_2ConfigRoot, ConfigRoot> {
    @Override
    public ConfigRoot upgradeVersion(V4_0_2ConfigRoot currentConfig) {
        Elevators.getElevatorsLogger().info("Converting config from V4.0.2 - V5.0.0");

        ConfigRoot newConfig = new ConfigRoot();
        newConfig.updateCheckerEnabled = currentConfig.updateCheckerEnabled;
        newConfig.forceFacingUpwards = currentConfig.forceFacingUpwards;

        ConfigLocale locale = new ConfigLocale();
        locale.cantCreateMessage = currentConfig.cantCreateMessage;
        locale.cantUseMessage = currentConfig.cantUseMessage;
        locale.cantGiveMessage = currentConfig.cantGiveMessage;
        locale.cantReloadMessage = currentConfig.cantReloadMessage;
        locale.notEnoughRoomGiveMessage = currentConfig.notEnoughRoomGiveMessage;
        locale.givenElevatorMessage = currentConfig.givenElevatorMessage;
        locale.worldDisabledMessage = currentConfig.worldDisabledMessage;

        newConfig.locale = locale;

        // We now support many different protection hooks, so lets set the "claimProtectionDefault" setting on all that are loaded.
        for (ProtectionHook hook : ElevatorHookService.getProtectionHooks()) {
            ConfigHookData hookData = new ConfigHookData();
            hookData.blockNonMemberUseDefault = currentConfig.claimProtectionDefault;

            if(newConfig.protectionHooks == null)
                newConfig.protectionHooks = new HashMap<>();

            newConfig.protectionHooks.put(hook.getConfigKey(), hookData);
        }

        newConfig.disabledWorlds = currentConfig.disabledWorlds;

        // Let's make our default effect :) Show them how it's done :)
        ConfigEffect creeperEffect = new ConfigEffect();
        creeperEffect.file = "Creeper.png";
        creeperEffect.scale = 1;
        creeperEffect.duration = 1.0F;
        creeperEffect.useHolo = false;
        creeperEffect.background = "#FFFFFF";

        newConfig.effects = new HashMap<>();
        newConfig.effects.put("creeper", creeperEffect);

        // It hurts to carry over the old perms, because the old perms do not take into account multiple elevator types. Oh well.

        ElevatorType defaultElevator = new ElevatorType();
        defaultElevator.usePermission = "elevators.use";
        defaultElevator.dyePermission = "elevators.dye";

        defaultElevator.displayName = currentConfig.displayName;
        defaultElevator.maxDistance = currentConfig.maxDistance;
        defaultElevator.maxSolidBlocks = currentConfig.maxSolidBlocks;
        defaultElevator.maxStackSize = currentConfig.maxStackSize;
        defaultElevator.stopObstruction = currentConfig.stopObstruction;
        defaultElevator.checkColor = currentConfig.checkColor;
        defaultElevator.checkPerms = currentConfig.checkPerms;
        defaultElevator.canExplode = currentConfig.canExplode;
        defaultElevator.loreLines = currentConfig.lore;

        defaultElevator.actions.up = currentConfig.actions.up;
        defaultElevator.actions.down = currentConfig.actions.down;

        defaultElevator.recipes = new HashMap<>();

        for (String recipeKey : currentConfig.recipes.keySet()) {
            V4_0_2ConfigRecipe currentRecipe = currentConfig.recipes.get(recipeKey);
            ElevatorRecipeGroup newRecipe = new ElevatorRecipeGroup();
            newRecipe.defaultOutputColor = DyeColor.valueOf(currentConfig.defaultColor);
            newRecipe.supportMultiColorOutput = currentRecipe.coloredCrafting;
            newRecipe.supportMultiColorMaterials = currentRecipe.coloredCrafting;
            newRecipe.recipe = currentRecipe.recipe;
            newRecipe.craftPermission = currentRecipe.permission;

            newRecipe.materials = currentRecipe.materials;

            defaultElevator.recipes.put(recipeKey, newRecipe);
        }

        newConfig.elevators = new HashMap<>();
        newConfig.elevators.put("DEFAULT", defaultElevator);

        defaultElevator.onLoad();

        return newConfig;
    }
}
