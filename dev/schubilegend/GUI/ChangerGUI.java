package dev.schubilegend.GUI;

import dev.schubilegend.SchubiAuth;
import dev.schubilegend.Utils.APIUtils;
import dev.schubilegend.Utils.SessionChanger;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public class ChangerGUI extends GuiScreen {
   private final GuiScreen previousScreen;
   private String status = "";
   private GuiTextField nameField;
   private GuiTextField skinField;
   private ScaledResolution sr;
   private ArrayList<GuiTextField> textFields = new ArrayList();

   public ChangerGUI(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   public void func_73866_w_() {
      Keyboard.enableRepeatEvents(true);
      this.sr = new ScaledResolution(this.field_146297_k);
      this.nameField = new GuiTextField(1, this.field_146297_k.field_71466_p, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2, 97, 20);
      this.nameField.func_146203_f(16);
      this.nameField.func_146195_b(true);
      this.skinField = new GuiTextField(2, this.field_146297_k.field_71466_p, this.sr.func_78326_a() / 2 + 3, this.sr.func_78328_b() / 2, 97, 20);
      this.skinField.func_146203_f(32767);
      this.textFields.add(this.nameField);
      this.textFields.add(this.skinField);
      this.field_146292_n.add(new GuiButton(3100, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2 + 25, 97, 20, "Change Name"));
      this.field_146292_n.add(new GuiButton(3200, this.sr.func_78326_a() / 2 + 3, this.sr.func_78328_b() / 2 + 25, 97, 20, "Change Skin"));
      this.field_146292_n.add(new GuiButton(3300, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2 + 50, 200, 20, "Back"));
      super.func_73866_w_();
   }

   public void func_146281_b() {
      Keyboard.enableRepeatEvents(false);
      super.func_146281_b();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.field_146297_k.field_71466_p.func_78276_b(this.status, this.sr.func_78326_a() / 2 - this.field_146297_k.field_71466_p.func_78256_a(this.status) / 2, this.sr.func_78328_b() / 2 - 40, Color.WHITE.getRGB());
      this.field_146297_k.field_71466_p.func_78276_b("Name:", this.sr.func_78326_a() / 2 - 99, this.sr.func_78328_b() / 2 - 15, Color.WHITE.getRGB());
      this.field_146297_k.field_71466_p.func_78276_b("Skin:", this.sr.func_78326_a() / 2 + 4, this.sr.func_78328_b() / 2 - 15, Color.WHITE.getRGB());
      this.nameField.func_146194_f();
      this.skinField.func_146194_f();
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      String newSkin;
      if (button.field_146127_k == 3100) {
         newSkin = this.nameField.func_146179_b();
         if (Objects.equals(SchubiAuth.originalSession.func_148254_d(), this.field_146297_k.func_110432_I().func_148254_d())) {
            this.status = "§4Prevented you from changing the name of your main account!";
         } else {
            (new Thread(() -> {
               try {
                  int statusCode = APIUtils.changeName(newSkin, this.field_146297_k.func_110432_I().func_148254_d());
                  if (statusCode == 200) {
                     this.status = "§2Successfully changed name!";
                     SessionChanger.setSession(new Session(newSkin, this.field_146297_k.func_110432_I().func_148255_b(), this.field_146297_k.func_110432_I().func_148254_d(), "mojang"));
                  } else if (statusCode == 429) {
                     this.status = "§4Error: Too many requests!";
                  } else if (statusCode == 400) {
                     this.status = "§4Error: Invalid name!";
                  } else if (statusCode == 401) {
                     this.status = "§4Error: Invalid token!";
                  } else if (statusCode == 403) {
                     this.status = "§4Error: Name is unavailable/Player already changed name in the last 35 days";
                  } else {
                     this.status = "§4An unknown error occurred!";
                  }
               } catch (Exception var3) {
                  this.status = "§4An unknown error occurred!";
                  var3.printStackTrace();
               }

            })).start();
         }
      }

      if (button.field_146127_k == 3200) {
         newSkin = this.skinField.func_146179_b();
         (new Thread(() -> {
            try {
               int statusCode = APIUtils.changeSkin(newSkin, this.field_146297_k.func_110432_I().func_148254_d());
               if (statusCode == 200) {
                  this.status = "§2Successfully changed skin!";
               } else if (statusCode == 429) {
                  this.status = "§4Error: Too many requests!";
               } else if (statusCode == 401) {
                  this.status = "§4Error: Invalid token!";
               } else {
                  this.status = "§4Error: Invalid Skin";
               }
            } catch (Exception var3) {
               this.status = "§4An unknown error occurred!";
               var3.printStackTrace();
            }

         })).start();
      }

      if (button.field_146127_k == 3300) {
         this.field_146297_k.func_147108_a(this.previousScreen);
      }

      super.func_146284_a(button);
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      this.nameField.func_146201_a(typedChar, keyCode);
      this.skinField.func_146201_a(typedChar, keyCode);
      if (1 == keyCode) {
         this.field_146297_k.func_147108_a(this.previousScreen);
      } else {
         super.func_73869_a(typedChar, keyCode);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      boolean prevFocused = false;
      boolean postFocused = false;

      GuiTextField text;
      for(Iterator var6 = this.textFields.iterator(); var6.hasNext(); postFocused = text.func_146206_l() || postFocused) {
         text = (GuiTextField)var6.next();
         prevFocused = text.func_146206_l() || prevFocused;
         text.func_146192_a(mouseX, mouseY, mouseButton);
      }

   }
}
