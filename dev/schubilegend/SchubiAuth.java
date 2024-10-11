package dev.schubilegend;

import dev.schubilegend.GUI.ChangerGUI;
import dev.schubilegend.GUI.SessionGUI;
import dev.schubilegend.Utils.APIUtils;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

@Mod(
   modid = "SchubiAuth",
   version = "2.0"
)
public class SchubiAuth {
   public static final String MODID = "SchubiAuth";
   public static final String VERSION = "2.0";
   public static Minecraft mc = Minecraft.func_71410_x();
   public static Session originalSession;
   public static String onlineStatus;
   public static String isSessionValid;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(this);
      Display.setTitle("SchubiAuth 2.0");
   }

   @SubscribeEvent
   public void onInitGuiPost(Post e) throws IOException, ParseException {
      if (e.gui instanceof GuiMultiplayer) {
         e.buttonList.add(new GuiButton(2100, e.gui.field_146294_l - 90, 5, 80, 20, "Login"));
         e.buttonList.add(new GuiButton(2200, e.gui.field_146294_l - 180, 5, 80, 20, "Changer"));
         (new Thread(() -> {
            try {
               isSessionValid = APIUtils.validateSession(mc.func_110432_I().func_148254_d()) ? "§2✔ Valid" : "§4╳ Invalid";
               onlineStatus = APIUtils.checkOnline(mc.func_110432_I().func_111285_a()) ? "§2✔ Online" : "§4╳ Offline";
            } catch (Exception var1) {
               var1.printStackTrace();
            }

         })).start();
      }

   }

   @SubscribeEvent
   public void onDrawScreenPost(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post e) throws IOException, ParseException {
      if (e.gui instanceof GuiMultiplayer) {
         Minecraft.func_71410_x().field_71466_p.func_78276_b("§fUser: " + mc.func_110432_I().func_111285_a() + "  §f|  " + onlineStatus + "  §f|  " + isSessionValid, 5, 10, Color.RED.getRGB());
      }

   }

   @SubscribeEvent
   public void onActionPerformedPre(Pre e) {
      if (e.gui instanceof GuiMultiplayer) {
         if (e.button.field_146127_k == 2100) {
            Minecraft.func_71410_x().func_147108_a(new SessionGUI(e.gui));
         }

         if (e.button.field_146127_k == 2200) {
            Minecraft.func_71410_x().func_147108_a(new ChangerGUI(e.gui));
         }
      }

   }

   static {
      originalSession = mc.func_110432_I();
      onlineStatus = "§4╳ Offline";
      isSessionValid = "§2✔ Valid";
   }
}
