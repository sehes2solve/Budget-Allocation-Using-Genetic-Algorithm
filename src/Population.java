import java.util.*;

public class Population {
    final static int populationSize=100;
    final static double crossOverProb=0.9;

    List<Chromosome> chromosomes=new ArrayList<>();
    void addChromosome(Chromosome chromosome){
        chromosomes.add(chromosome);
    }
    static Population createInitialPopulation(List<Channel> channels, double budget){
        Population population=new Population();
        for(int i=0 ; i<populationSize ; i++) {
            population.addChromosome(Chromosome.generateRandom(channels, budget));
        }
        return population;
    }
    Population tournametSelection(){
        Population matingPoll=new Population();
        // we check every two consecutive and take the best two out of them
        for (int i = 0; i < chromosomes.size(); i+=2) {
            Chromosome chromosome1=chromosomes.get(i);
            Chromosome chromosome2=chromosomes.get(i+1);
            if(chromosome1.calculateFitness()>=chromosome2.calculateFitness()){
                matingPoll.addChromosome(chromosome1);
            }else{
                matingPoll.addChromosome(chromosome2);
            }
        }
        return matingPoll;
    }
    // this method is called from the mating poll
    Population applyCrossover(boolean uniformMutation,double budget,int generation){
        Random random=new Random();
        Population population=new Population();
        for(int i=1 ; i<chromosomes.size() ; i++){
            for(int j=0 ; j<i ; j++){
                if(random.nextDouble()>=crossOverProb)continue;
                Chromosome chromosome1 = chromosomes.get(i);
                Chromosome chromosome2 = chromosomes.get(j);
                Chromosome child=chromosome1.crossover2Point(chromosome2);
                if(uniformMutation)
                    child.mutateUniform();
                else
                    child.mutateNonUniform(generation);

                // we apply the death penalty for infeasible solutions
                if(child.isChromosomeValid(budget))
                    population.addChromosome(child);
            }
        }
        return population;
    }

}
