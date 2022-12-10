import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>{
    final static double mutationProbability=0.1;
    // this parameter is used in the non uniform mutation
    final static Double R=new Random().nextDouble();
    public Chromosome(){}
    public Chromosome(Map<Channel, Double> inv) {
        this.inv = inv;
    }
    Map<Channel, Double> inv;
    void addGene(Channel channel,Double val){
        if(inv ==null){
            inv =new HashMap<>();
        }
        inv.put(channel,val);
    }
    public void mutateUniform(){
        Random random=new Random();
        for(Channel channel: inv.keySet()){
            if(random.nextDouble()>=mutationProbability)continue;
            if(random.nextDouble()<0.5){
                double x=inv.get(channel);
                double delta=x-channel.lowerInv;
                double rand=random.nextDouble()*delta;
                inv.put(channel, x-rand);
            }else{
                double x=inv.get(channel);
                double delta=channel.upperInv-x;
                double rand=random.nextDouble()*delta;
                inv.put(channel, x+rand);
            }
        }
    }
    public void mutateNonUniform(int generation){
        Random random=new Random();
        double parameter=1-Math.pow(random.nextDouble(),1-(double)generation/Main.numberOfGenerations);
        for(Channel channel: inv.keySet()){
            if(random.nextDouble()>=mutationProbability)continue;
            if(random.nextDouble()<0.5){
                double x=inv.get(channel);
                double delta=x-channel.lowerInv;
                delta*=parameter;
                double rand=random.nextDouble()*delta;
                inv.put(channel, x-rand);
            }else{
                double x=inv.get(channel);
                double delta=channel.upperInv-x;
                delta*=parameter;
                double rand=random.nextDouble()*delta;
                inv.put(channel, x+rand);
            }
        }
    }
    public Double calculateFitness() {
        double res = 0;
        for (Channel channel : inv.keySet()) {
            res += channel.ROI * 0.01 * inv.get(channel);
        }
        return res;
    }
    public Chromosome crossover2Point(Chromosome chromosome){
        Random random=new Random();
        int point1 = random.nextInt(this.inv.size()-1);
        int point2 = random.nextInt(this.inv.size()-point1-1)+1;
        Chromosome result=new Chromosome();
        int i=0;
        for(Channel channel: inv.keySet()){
            if(i<=point1){
                result.addGene(channel,this.inv.get(channel));
            }else if(i<=point2){
                result.addGene(channel,chromosome.inv.get(channel));
            }else{
                result.addGene(channel,this.inv.get(channel));
            }
            i++;
        }
        return result;
    }
    public static Chromosome generateRandom(List<Channel> channels, double budget) {
        Map<Channel, Double> inv = new HashMap<>();
        while (true) {
            for (Channel channel : channels) {
                inv.put(channel, channel.getRandomValueInRange());
            }
            if (isChromosomeValid(inv, budget)) break;
        }
        return new Chromosome(inv);
    }
    boolean isChromosomeValid(double budget){
        return isChromosomeValid(inv,budget);
    }
    static boolean isChromosomeValid(Map<Channel, Double> inv, double budget) {
        double sum = 0;
        for (Channel channel : inv.keySet()) {
            sum += inv.get(channel);
        }
        return sum <= budget;
    }

    @Override
    public int compareTo(Chromosome o) {
        return calculateFitness().compareTo(o.calculateFitness());
    }
}
