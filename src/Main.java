import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    final static int numberOfGenerations = 100;
    static Scanner scanner=new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Enter the marketing budget (in thousands):");
        double budget = scanner.nextDouble();
        List<Channel> channels = getChannels(budget);
        double mx=0;
        int mxInd=0;
        PrintStream console = System.out;
        for(int i=0 ; i<20 ; i++) {
            File file = new File("output/output"+i);
            PrintStream stream = new PrintStream(file);
            System.setOut(stream);
            double x=run(budget, channels,true);
            if(x>=mx){
                mx=x;
                mxInd=i;
            }
        }
        for(int i=0 ; i<20 ; i++) {
            File file = new File("output/output"+(i+20));
            PrintStream stream = new PrintStream(file);
            System.setOut(stream);
            double x=run(budget, channels,false);
            if(x>=mx){
                mx=x;
                mxInd=i+20;
            }
        }
        System.setOut(console);
        System.out.println("best solution ----");
        // printing the best solution
        Scanner scanner=new Scanner(new File("output/output"+mxInd));
        while(scanner.hasNext()){
            System.out.println(scanner.nextLine());
        }
    }

    private static double run(double budget, List<Channel> channels,boolean uniformMutation) {
        Population currentPop = Population.createInitialPopulation(channels, budget);
        for (int i = 0; i <= numberOfGenerations; i++) {
            Population matingPoll = currentPop.tournametSelection();
            Population children = matingPoll.applyCrossover(uniformMutation, budget, i);
            currentPop = applyReplacementAndGetTheNewPopulation(currentPop, children);
        }
        Collections.sort(currentPop.chromosomes, Collections.reverseOrder());
        Chromosome solution = currentPop.chromosomes.get(0);
        System.out.println(solution.calculateFitness() + "k");
        for (Channel channel : solution.inv.keySet()) {
            System.out.println(channel.name + " -> " + solution.inv.get(channel) + "k");
        }
        return solution.calculateFitness();
    }

    private static List<Channel> getChannels(double budget) {
        System.out.println("Enter the number of marketing channels:");
        int numberOfChannels = scanner.nextInt();
        List<Channel> channels = new ArrayList<>();
        System.out.println("Enter the name and ROI (in %) the lower (k) and upper bounds (%) of investment of each channel separated by space:");
        scanner.nextLine();
        for (int i = 0; i < numberOfChannels; i++) {
            channels.add(Channel.readChannel(budget));
        }
        return channels;
    }

    public static Population applyReplacementAndGetTheNewPopulation(Population current,Population children) {
        Population population=new Population();
        List<Chromosome> chromosomes=new ArrayList<>();
        chromosomes.addAll(current.chromosomes);
        chromosomes.addAll(children.chromosomes);
        Collections.sort(chromosomes,Collections.reverseOrder());
        for(int i=0 ; i<Population.populationSize ; i++){
            population.addChromosome(chromosomes.get(i));
        }
        return population;
    }
}