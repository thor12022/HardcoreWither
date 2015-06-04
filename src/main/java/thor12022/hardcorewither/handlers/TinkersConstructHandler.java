package thor12022.hardcorewither.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tconstruct.armor.TinkerArmor;
import tconstruct.util.ItemHelper;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import thor12022.hardcorewither.config.ConfigManager;
import thor12022.hardcorewither.config.IConfigClass;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.ModInformation;
import thor12022.hardcorewither.potions.PotionAntiWither;
import thor12022.hardcorewither.potions.PotionRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class TinkersConstructHandler implements IConfigClass
{
   private static TinkersConstructHandler eventHandler = new TinkersConstructHandler();
   
   private static boolean enableGreenHeartCanister = true;
   private static boolean enableGreenHeartWitherDrop = true;
   private static int greenHeartDropRarity = 2;
   
   private TinkersConstructHandler()
   {
      MinecraftForge.EVENT_BUS.register(this);
      ConfigManager.getInstance().addConfigClass(this);
   }
   
   public static void init(FMLInitializationEvent event)
   {
      if(enableGreenHeartCanister)
      {
         GameRegistry.addShapelessRecipe( new ItemStack(TinkerArmor.heartCanister, 1, 6), 
                                          new ItemStack(TinkerArmor.heartCanister, 1, 4), 
                                          new ItemStack(TinkerArmor.heartCanister, 1, 5), 
                                          new ItemStack(Items.nether_star));
      }
   }
   
   @SubscribeEvent
   public void onLivingDrop (LivingDropsEvent event)
   {
       if (!enableGreenHeartWitherDrop || !event.recentlyHit)
       {
          return;
       }
       //FeedTheCreeperTweaks.logger.debug("EntityLiving Dropping something");
       if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
       {
          //FeedTheCreeperTweaks.logger.debug("It was a Wither");
          if(event.source.damageType.equals("player"))
          {
             //FeedTheCreeperTweaks.logger.debug("A Player did it");
             EntityPlayer killerPlayer = (EntityPlayer)event.source.getEntity();
             PotionEffect antiWitherEffect = killerPlayer.getActivePotionEffect(PotionRegistry.potionAntiWither);
             if( antiWitherEffect != null )
             {
                //FeedTheCreeperTweaks.logger.debug("They used Anti-Wither!");
                if(PotionAntiWither.HasEntityBeenWithered(killerPlayer))
                {
                   //FeedTheCreeperTweaks.logger.debug("They were withered!");
                   Random rand = new Random();
                   int numberOfHearts = 0;
                   for( int lootingLevel = event.lootingLevel; lootingLevel > 0; --lootingLevel)
                   {
                      numberOfHearts += rand.nextInt(greenHeartDropRarity) == 0 ? 1 : 0;
                   }
                   ItemHelper.addDrops(event, new ItemStack(TinkerArmor.heartCanister, numberOfHearts, 5));
                   HardcoreWither.logger.debug("Withered Anti-Withered Player killed Wither, dropping Miniture" + numberOfHearts + " Green Hearts");
                }
             }
             
          }
       }
    }

   @Override
   public void syncConfig(Configuration config)
   {
      enableGreenHeartCanister = config.getBoolean("Enable Green Heart Canister Crafting",getSectionName(), enableGreenHeartCanister, "Requires Tinkers' Construct");
      enableGreenHeartWitherDrop = config.getBoolean("Enable Withers Dropping Green Hearts",getSectionName(), enableGreenHeartWitherDrop, "Requires Tinkers' Construct");
      greenHeartDropRarity = config.getInt("Green Heart Drop Rarity", getSectionName(), greenHeartDropRarity, 0, Integer.MAX_VALUE, "How rare the Green Heart drop is, 0 is a guarnenteed 1 per level of fortune");
      
   }

   @Override
   public String getSectionName()
   {
      return "TinkersConstruct";
   }
}
