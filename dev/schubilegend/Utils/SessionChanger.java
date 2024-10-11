package dev.schubilegend.Utils;

import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SessionChanger {
   public static void setSession(Session session) {
      Field sessionField = ReflectionHelper.findField(Minecraft.class, new String[]{"session", "field_71449_j"});
      ReflectionHelper.setPrivateValue(Field.class, sessionField, sessionField.getModifiers() & -17, new String[]{"modifiers"});
      ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.func_71410_x(), session, new String[]{"session", "field_71449_j"});
   }
}
