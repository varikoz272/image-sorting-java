import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public final class ImageSorter extends Canvas implements KeyListener {
   private BufferedImage img;
   public int[] pixels;
   private int WIDTH;
   private int HEIGHT;
   public static int theta = 0;
   private static int speed = 200;
   private SortingMethod method;
   private SortingOrderMethod orderMethod;

   public ImageSorter(File var1, SortingMethod var2, SortingOrderMethod var3) {
      try {
         this.method = var2;
         this.orderMethod = var3;
         BufferedImage var4 = ImageIO.read(var1);
         this.img = new BufferedImage(var4.getWidth(), var4.getHeight(), 1);
         this.img.getGraphics().drawImage(var4, 0, 0, null);
         this.pixels = ((DataBufferInt)this.img.getRaster().getDataBuffer()).getData();
         this.WIDTH = this.img.getWidth();
         this.HEIGHT = this.img.getHeight();
      } catch (IOException var5) {}

   }

   private void renderState() {
      BufferStrategy var1 = this.getBufferStrategy();
      if (var1 == null) {
         this.createBufferStrategy(3);
      } else {
         Graphics var2 = var1.getDrawGraphics();
         var2.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), (ImageObserver)null);
         var2.dispose();
         var1.show();
      }
   }

   private static int getRGBSum(int var0) {
      int var1 = var0 >> 16 & 255;
      int var2 = var0 >> 8 & 255;
      int var3 = var0 & 255;
      return var1 + var2 + var3;
   }

   public static int[] getRGB(int var0) {
      return new int[]{var0 >> 16 & 255, var0 >> 8 & 255, var0 & 255};
   }

   private static int getR(int var0) {
      return getRGB(var0)[0];
   }

   private static int getG(int var0) {
      return getRGB(var0)[1];
   }

   private static int getB(int var0) {
      return getRGB(var0)[2];
   }

   public void start() {
      this.method.sort(this, this.orderMethod);
   }

   public static void bubbleSort(ImageSorter var0, SortingOrderMethod var1) {
      int[] var2 = var0.pixels;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         for(int var4 = 0; var4 < var2.length - var3 - 1; ++var4) {
            if (var1.getValue(var2[var4]) > var1.getValue(var2[var4 + 1])) {
               swap(var2, var4, var4 + 1);
            }
         }

         if (theta++ % speed == 0) {
            var0.renderState();
         }
      }

   }

   public static void quickSort(ImageSorter var0, SortingOrderMethod var1) {
      quickSort(var0, 0, var0.pixels.length - 1, var1);
   }

   private static void quickSort(ImageSorter var0, int var1, int var2, SortingOrderMethod var3) {
      if (var1 < var2) {
         int[] var4 = var0.pixels;
         int var5 = var3.getValue(var4[var2]);
         int var6 = var1;
         int var7 = var2;

         while(var6 < var7) {
            while(var3.getValue(var4[var6]) <= var5 && var6 < var7) {
               ++var6;
            }

            while(var3.getValue(var4[var7]) >= var5 && var6 < var7) {
               --var7;
            }

            swap(var4, var6, var7);
            if (theta++ % speed == 0) {
               var0.renderState();
            }
         }

         swap(var4, var6, var2);
         quickSort(var0, var1, var6 - 1, var3);
         quickSort(var0, var6 + 1, var2, var3);
      }
   }

   public static void combSort(ImageSorter var0, SortingOrderMethod var1) {
      int[] var2 = var0.pixels;

      for(int var3 = var2.length / 2; var3 > 0; --var3) {
         for(int var4 = 0; var4 < var2.length - var3; ++var4) {
            if (var1.getValue(var2[var4]) > var1.getValue(var2[var4 + var3])) {
               swap(var2, var4, var4 + var3);
            }
         }

         if (theta++ % speed == 0) {
            var0.renderState();
         }
      }

   }

   public static void coctailShakerSort(ImageSorter var0, SortingOrderMethod var1) {
      int[] var2 = var0.pixels;
      int var3 = var2.length;

      for(int var4 = 0; var4 != var3; ++var4) {
         int var5;
         for(var5 = var4; var5 < var3 - 1; ++var5) {
            if (var1.getValue(var2[var5]) > var1.getValue(var2[var5 + 1])) {
               swap(var2, var5, var5 + 1);
            }

            if (theta++ % speed == 0) {
               var0.renderState();
            }
         }

         --var3;

         for(var5 = var3; var5 >= var4 + 1; --var5) {
            if (var1.getValue(var2[var5]) < var1.getValue(var2[var5 - 1])) {
               swap(var2, var5, var5 - 1);
            }

            if (theta++ % speed == 0) {
               var0.renderState();
            }
         }
      }

   }

   public static void swap(int[] var0, int var1, int var2) {
      int var3 = var0[var1];
      var0[var1] = var0[var2];
      var0[var2] = var3;
   }

   public static void main(String[] var0) {
      if (!argsContains(var0, "-h") && !argsContains(var0, "-help")) {
         if (!argsContains(var0, "-sh") && !argsContains(var0, "-shelp")) {
            if (!argsContains(var0, "-oh") && !argsContains(var0, "-ohelp")) {
               if (var0.length == 0) {
                  System.out.println("You should pass at least 1 argument");
               } else {
                  if (var0.length > 0) {
                     File var1 = new File(var0[0]);
                     JFrame var2 = new JFrame();
                     var2.setBounds(0, 0, 500, 500);
                     ImageSorter var3 = new ImageSorter(var1, getSortMethodByName(""), getSortOrderMethodByName(""));
                     var3.addKeyListener(var3);
                     if (var0.length == 3) {
                        var3 = new ImageSorter(var1, getSortMethodByName(var0[1]), getSortOrderMethodByName(var0[2]));
                     }

                     if (var0.length == 2) {
                        new ImageSorter(var1, getSortMethodByName(var0[1]), getSortOrderMethodByName(""));
                     }

                     var2.add(var3);
                     var2.addKeyListener(var3);
                     var2.setLocationRelativeTo((Component)null);
                     var2.setDefaultCloseOperation(3);
                     var2.setVisible(true);
                     var3.start();
                  }

               }
            } else {
               System.out.println();
               System.out.println("Order is the final result");
               System.out.println();
               System.out.println("Avaible orders:");
               System.out.println("- red");
               System.out.println("- green");
               System.out.println("- blue");
               System.out.println("- sum");
               System.out.println("-");
               System.out.println("-");
               System.out.println("-");
               System.out.println();
               System.out.println("Type letters without '-' to use any order");
            }
         } else {
            System.out.println();
            System.out.println("Sorting is the way of moving pixels");
            System.out.println();
            System.out.println("Avaible sort methods:");
            System.out.println("- bubble");
            System.out.println("- comb");
            System.out.println("- quick");
            System.out.println("-");
            System.out.println("-");
            System.out.println("-");
            System.out.println();
            System.out.println("Type letters without '-' to use any sort method");
         }
      } else {
         System.out.println();
         System.out.println("Usage:");
         System.out.println("Type name of an image file (for example \"C:\\users\\<name>\\desktop\\picture_on_your_desktop.png\")");
         System.out.println();
         System.out.println("You can additionaly add 2 arguments:");
         System.out.println("- The name of sorting (-sh or -shelp)");
         System.out.println("- The ordering (-oh or -ohelp)");
         System.out.println();
         System.out.println("Example:");
         System.out.println("java ImageSorter \"C:\\users\\admin\\desktop\\photo.jpg bubble red\"");
      }
   }

   public static boolean argsContains(String[] var0, String var1) {
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static SortingOrderMethod getSortOrderMethodByName(String var0) {
      byte var2 = -1;
      switch(var0.hashCode()) {
      case 112785:
         if (var0.equals("red")) {
            var2 = 0;
         }
         break;
      case 114251:
         if (var0.equals("sum")) {
            var2 = 3;
         }
         break;
      case 3027034:
         if (var0.equals("blue")) {
            var2 = 2;
         }
         break;
      case 98619139:
         if (var0.equals("green")) {
            var2 = 1;
         }
      }

      switch(var2) {
      case 0:
         return ImageSorter::getR;
      case 1:
         return ImageSorter::getG;
      case 2:
         return ImageSorter::getB;
      case 3:
         return ImageSorter::getRGBSum;
      default:
         return ImageSorter::getRGBSum;
      }
   }

   public static SortingMethod getSortMethodByName(String var0) {
      byte var2 = -1;
      switch(var0.hashCode()) {
      case -1378241396:
         if (var0.equals("bubble")) {
            var2 = 0;
         }
         break;
      case 3059457:
         if (var0.equals("comb")) {
            var2 = 1;
         }
         break;
      case 107947501:
         if (var0.equals("quick")) {
            var2 = 2;
         }
         break;
      case 941367879:
         if (var0.equals("coctail")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
         return ImageSorter::bubbleSort;
      case 1:
         return ImageSorter::combSort;
      case 2:
         return ImageSorter::quickSort;
      case 3:
         return ImageSorter::coctailShakerSort;
      default:
         return ImageSorter::quickSort;
      }
   }

   public void keyPressed(KeyEvent var1) {
      if (var1.getKeyCode() == 49) {
         speed += 50;
         System.out.println(speed);
      }

      if (var1.getKeyCode() == 50 && speed >= 100) {
         speed -= 50;
         System.out.println(speed);
      }

   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
   }

    @FunctionalInterface
    public interface SortingMethod {
    void sort(ImageSorter var1, SortingOrderMethod var2);
    }

    @FunctionalInterface
    public interface SortingOrderMethod {
        int getValue(int var1);
    }
    
}