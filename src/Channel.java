import java.util.Random;
import java.util.Scanner;

public class Channel {
    String name;
    double lowerInv, upperInv, ROI;

    // name ROI lowerInv upperPerc
    // facebook 12 100 30
    // we take the totalInv parameter to calculate the
    // upperInv based on it
    public static Channel readChannel(double totalInv) {
        Channel channel = new Channel();
        String line = Main.scanner.nextLine();
        String[] arr = line.split(" ");
        channel.name = arr[0];
        channel.ROI = Double.parseDouble(arr[1]);
        if (!arr[2].equals("x")) {
            channel.lowerInv = Double.parseDouble(arr[2]);
        } else {
            channel.lowerInv = 0;
        }
        if(!arr[3].equals("x")) {
            channel.upperInv = Double.parseDouble(arr[3]) * 0.01 * totalInv;
        }else{
            channel.upperInv=totalInv;
        }
        return channel;
    }
    public double getRandomValueInRange(){
        Random random=new Random();
        double rand = random.nextDouble();
        // rand will be in [0,1]
        rand *= (this.upperInv - this.lowerInv);
        return rand+this.lowerInv;
    }
}
