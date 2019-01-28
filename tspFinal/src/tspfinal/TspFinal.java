/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tspfinal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author bartosz
 */
public class TspFinal 
{
    public static void displayArray(int [][] tab){
        for(int i = 0; i <tab.length;i++){
            for(int j = 0; j < tab.length;j++)
            {
              System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void displayArrays(int [][] tab){
        for(int i = 0; i <tab.length;i++){
            for(int j = 0; j < tab[i].length;j++)
            {
              System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void displayOneDimensionArray(int[] tab)
    {
        for(int i = 0; i < tab.length;i++)
        {
         System.out.print(tab[i] + " ");    
        }
        System.out.println();
    }
    
    //ReadFile    
    public static int[][] makeArray(String name) throws FileNotFoundException, IOException
    {
        BufferedReader file = null;
        int[][] array = null;
        
        try
        {
            file= new BufferedReader(new FileReader(name));
            String line = file.readLine();
            if(line != null)
            {
                int size = Integer.parseInt(line);
                array = new int[size][size];
                
                String [] rowArray = null;
                int value = 0;
                line = file.readLine();
                
                for(int i = 0; i < array.length;i++){
                    rowArray = line.split(" ");
                    for(int j = 0;j < rowArray.length;j++)
                    {
                        value = Integer.parseInt(rowArray[j]);
                        array[j][i] = value;
                        array[i][j] = value;
                    }
                    line = file.readLine();
                }
            }
        }
        finally 
        {
            if(file != null){
                file.close();
            }
        }
        return array;
    }
    //GE
    
    //generuje tablice bez powtórzeń
    public static int[] generateRandomTmp(int[][] array,int[] tmp)
    {
        
        Random rnd = new Random();
        for(int i = 0; i < tmp.length;i++)
        {
            tmp[i] = -1;
            
        }
        int tmpRnd = rnd.nextInt(array.length);
        
        int counter = 0;
        boolean status = true;
        
        for (int i = 0; i < array.length - 2; i++)
        {   
            if (status)
            {
                if (tmpRnd == tmp[i])
                {   
                    tmpRnd = rnd.nextInt(array.length);
                    
                    i = -1;
                }
            
                else if (tmpRnd != tmp[i] && tmp[i] == -1)
                {
                    tmp[i] = tmpRnd;
                    tmpRnd = rnd.nextInt(array.length);                   
                    i = -1;
                    counter++;
                    if (counter == tmp.length - 1)
                    {
                        status = false;
                    }
                }   
            }
        }
        
        
        return tmp;
    }
    //stworzenie osobnika
    public static int[] makeSpecimen (int[][] array)
    {
      
       
       int[] chosenSpecimen = new int[array.length+1];
       
       Random rnd = new Random();                
       int rndNumber = rnd.nextInt(array.length - 1) + 1;      
       
       
       int tmpN = rndNumber;
      
       int distance = array[0][rndNumber];
       int sumDistance = distance;
       int variable = rndNumber;       
       chosenSpecimen[0] = rndNumber;
       
       
       boolean status = true;
       int[] tmp = new int[array.length + 1];
       for(int i = 0; i < tmp.length;i++)
        {
            tmp[i] = -1;
            
        }
        rndNumber = rnd.nextInt(array.length - 1) + 1;
        
        int counter = 0;
        
        
        for (int i = 0; i < array.length - 2; i++)
        {   
            if (status)
            {
              if(rndNumber != tmpN)
                {
                if (rndNumber == tmp[i])
                {   
                    rndNumber = rnd.nextInt(array.length - 1) + 1;
                   
                    i = -1;
                }  
                    else if (rndNumber != tmp[i] && tmp[i] == -1)
                {
                    distance = array[variable][rndNumber];
                    sumDistance += distance;
                    variable = rndNumber;                    
                    chosenSpecimen[i + 1] = rndNumber;   
                    
                    tmp[i] = rndNumber;
                    rndNumber = rnd.nextInt(array.length - 1) + 1;
                    
                    i = -1;
                    counter++;
                    
                    if (counter == tmp.length - 1)
                    {
                        status = false;
                    }
                }                
            } else {rndNumber = rnd.nextInt(array.length - 1) + 1;}
          }
        }           
       distance = array[0][rndNumber];
       sumDistance += distance;
       chosenSpecimen[chosenSpecimen.length - 1] = sumDistance;
           
       return chosenSpecimen;
    }
    
    //tworzenie populacji
    public static int[][] makeSpecies(int sizeOfSpecies,int[][] array)
    {
        int[] chosenSpecimen = null;
        int xD = 0;
        int[][] population = new int[sizeOfSpecies][array.length + 1];
        for(int i = 0; i < sizeOfSpecies;i++)
        {
            xD = 0;
            chosenSpecimen = makeSpecimen(array);
            for(int j = 0; j < array.length+1;j++)
            {
                
                if(chosenSpecimen[1] == chosenSpecimen[j])
                {
                    xD += 1;
                    
                }
                if(xD == 2 && j == array.length && i != 0)
                {
                    i -= 1;
                }
               population[i][j] = chosenSpecimen[j];
                 if(xD == 2 && j == array.length && i == 0)
                {
                    i = 0;
                }
            }
            
        }
        
        return population;
    }
    
    //Selekcja Turniejowa
    public static int[][] tournamentSelection (int [][] array, int [][] population)
    {
        int [][] tournament = new int [population.length][array.length + 1];
        Random rnd = new Random();       
        
         
        for (int i = 0; i < population.length; i++)
        {
           int x = rnd.nextInt(population.length - 1);
           int y = rnd.nextInt(population.length - 1);
           int z = rnd.nextInt(population.length - 1);
           int w = rnd.nextInt(population.length - 1);
           int v = rnd.nextInt(population.length - 1);
         
          int distanceX = population[x][array.length];
          int distanceY = population[y][array.length];
          int distanceZ = population[z][array.length];
          int distanceW = population[w][array.length];
          int distanceV = population[v][array.length];
          
          int alleyOne = Math.min(distanceX,distanceY);
          int alleyTwo = Math.min(distanceZ,distanceW);
          int alleyThree = Math.min(distanceV,alleyOne);
           
          int alley = Math.min(alleyThree, alleyTwo);
            
            if (distanceX == alley)
            {
                for (int j = 0; j < array.length + 1; j++)
                {
                    tournament[i][j] = population[x][j];
                }
            }
            else if (distanceY == alley)
            {
                for (int j = 0; j < array.length + 1; j++)
                {
                    tournament[i][j] = population[y][j];
                }
            }
            else if (distanceZ == alley)
            {
                for (int j = 0; j < array.length + 1; j++)
                {
                    tournament[i][j] = population[z][j];
                }
            }
            else if (distanceW == alley)
            {
                for (int j = 0; j < array.length + 1; j++)
                {
                    tournament[i][j] = population[w][j];
                }
            }
            else if (distanceV == alley)
            {
                for (int j = 0; j < array.length + 1; j++)
                {
                    tournament[i][j] = population[v][j];
                }
            }            
        }
        
         return tournament;
     }
  /*
    //selekcja ruletką 
    public static int[][] rouletteWheelSelection (int[][] arr,int[][] population)
    {
        int [][] rouletteWheel = new int [population.length][arr.length + 1];
        int [][] tmpArr = new int [population.length][arr.length + 1];
        int bc = 0;
        int individual = 0;
        int fitness= 0;
        int total= 0;
        Random rnd = new Random();
        
        for (int i = 0; i < population.length; i++)
        {
            individual = population[i][arr.length];
            if (individual > bc)
            {
                bc = individual;
            }
        }
        
        for (int i = 0; i < population.length; i++)
        {
            individual = population[i][arr.length];
            fitness= bc + 1 - individual; 
            
            for (int j = 0; j < arr.length+1; j++)
            {
                tmpArr[i][j] = population[i][j];
            }
            
            if (i == 0)
            {
                tmpArr[i][arr.length] = fitness;
            }
            else 
            {
                tmpArr[i][arr.length] = fitness+ tmpArr[i - 1][arr.length];
            }
            total+= fitness;
        }
        
        ??????????????????????????????????????????????????
        
        return rouletteWheel;
        
    }
    */
     //krzyzowanie uobx ( uniform order-based crossover)
     public static int[][] crossover(int[][] arr,int[][] population,int popSize,int rate)
    {
       Random rndNumber = new Random();
       
       int[] OIarray = new int[arr.length+1];
       
       int[][] crossArray = new int[population.length][arr.length+1];
       
       for(int i =0;i < popSize;i++)
       {
           for(int j = 0; j < arr.length +1;j++)
           {
               crossArray[i][j] = -1;
               
           }
       }
      
       for(int i = 0; i < population.length;i++)
       {
          
           if(i % 2 == 0)
           { 
               for(int x = 0; x < arr.length;x++)
               {
                  
                   int number = rndNumber.nextInt(rate);
                   
                   if(number == 0)
                   {
                       OIarray[x] = 0;
                   }
                   else 
                   {
                       OIarray[x] = 1;
                   }
               }                               
           }
          
           for(int j = 0;j < arr.length;j++)
           {
               if(OIarray[j] == 1)
               {
                   crossArray[i][j] = population[i][j];
                }
               
           }
        
        int[] temporaryArray = new int[arr.length];
        boolean status = true;
        int minusOne = -1;
        
        
         for (int j=0;j<arr.length;j++)
            {
                temporaryArray[j]=crossArray[i][j];
            }
           
            
            while (status)
            {                
                for (int j=0;j<arr.length;j++)
                {
                    
                    if(crossArray[i][j] == -1)
                    {                       
                       minusOne = j;
                       j = arr.length+1;
                       status = true;
                    }
                    else
                    {
                        status = false;
                    }                                    
                }
              
                if(minusOne != -1)
                {
                    if(i%2==1)
                    {
                        for(int x=0;x < arr.length;x++)
                        {                            
                            
                            int select = population[i-1][x];
                            int rpt = 0;
                            for(int y = 0;y < arr.length;y++)
                            {                                  
                                if (temporaryArray[y] != select)
                                {                                   
                                    rpt++;                                                                      
                                }
                               
                                if (arr.length == rpt)
                                {                                   
                                    crossArray[i][minusOne] = select;
                                    temporaryArray[minusOne]= select;
                                    x=arr.length+1;
                                    y=arr.length+1;
                                }
                            }
                        }
                    }
                    
                 else if(i%2==0)
                    {                   
                        for(int x=0;x<arr.length;x++)
                        {   
                            int select = population[i+1][x];                         
                            int rpt = 0;
                            
                            for(int y=0;y<arr.length;y++)
                            {                                  
                                if (temporaryArray[y] != select)
                                {                                   
                                    rpt++;                                    
                                }
                                if (temporaryArray.length == rpt)
                                {    
                                    crossArray[i][minusOne] = select;
                                    temporaryArray[minusOne]= select;
                                    x=arr.length+1;
                                    y=arr.length+1;
                                }
                            }
                        }
                    }                    
                }
            }
        }
    
       return crossArray;
    }
   //Mutacja 
    public static  int[][] mutations(int [][]arr,int[][] population,int popSize,int mutationRate)
    {
        Random rnd = new Random();     
        int total  = 0; 
        
        for(int i = 0; i < popSize;i++)
     {  
        
        int rate = rnd.nextInt(mutationRate);
        
        if(rate == 1)
        {
            
         int mutationX = 1;
         int mutationY = 1;
         int changer  = 0;
         int temporary = 0;
         while(mutationX == mutationY)
         {
             mutationX = rnd.nextInt(arr.length);
             mutationY = rnd.nextInt(arr.length);
             if(mutationX < mutationY)
             {
                 changer = mutationY;
                 mutationY = mutationX;
                 mutationX = changer;
             }
        }
         temporary = 0;
         while(mutationY < mutationX)
         {
             temporary = population[i][mutationY];
             population[i][mutationY] = population[i][mutationX];
             population[i][mutationX] = temporary;
             mutationY++;
             mutationX--;
         }
        }
     }
      
     
     for(int i = 0; i < popSize;i++)
     {
        total = 0;
         int x = 0;
         
         for(int j = 0;j<arr.length;j++)
         {
             
             if(j == 0)
             {
                 x = population[i][arr.length -1];
             }
             else
             {
                x =population[i][j-1]; 
             }
             int y = population[i][j];
             total += arr[x][y];
         }
         population[i][arr.length] = total;
     }
     
     return population;
    }
    
    //wybranie najlepszego osobnika
     public static int [] optimal(int[][] array,int[][] population)
    {
       
        int[] optimalArray = new int[array.length + 1]; 
        int number = 100000;
        for (int i = 0; i < population.length; i++)
            {
                while (population[i][array.length] < number)
                {
                    number = population[i][array.length];
                    
                    for (int j = 0; j < array.length + 1; j++ )
                    {
                        optimalArray[j] = population[i][j];
                    }
                    
                    optimalArray[array.length] = number;
                }
            }
        
        return optimalArray;       
    }
     
     //zapis osobnika do pliku
     public static void saveOptimal (int[][] array,int[] optimalArray,String ex) throws FileNotFoundException
     {
         System.out.println("Zapisywanie najlepszego osobnika");
         PrintWriter save = new PrintWriter(ex);
        for(int i = 0; i < array.length + 1;i++)
        {
            save.print(optimalArray[i]);
            
            if(i < array.length - 1)
            {
                save.print("-");                
            } 
            else
            {
                save.print(" ");
            }
        }
        save.close();
         
     }
    
     
    
    public static void main(String[] args) throws IOException
    {
        int[][] arr = null;
       // String fileName = "R:\\berlin52.txt";
       //String fileName = "R:\\att48.txt";
        //String fileName = "R:\\eil51.txt";
        String fileName = "R:\\gr666.txt";
       // String fileName = "R:\\kroA100.txt";
        //String fileName = "R:\\pr107.txt";
        //String fileName = "R:\\a280.txt";
        //String fileName = "R:\\kroC100.txt";
        //String fileName = "R:\\pr76.txt";
        arr  = makeArray(fileName);
       
       int[][] population = makeSpecies(40,arr);
       displayArrays(population);
        
        int[] optimalArray = null;
        

        for(int i = 0; i < 1000;i++)
        {
            population = tournamentSelection(arr,population);
            population = crossover(arr,population,40,60);
            population = mutations(arr,population,40,60);
            optimalArray = optimal(arr,population);
        }
    
    saveOptimal(arr,optimalArray,"R:\\scores\\gr666_wynik.txt");
    displayOneDimensionArray(optimalArray);
        
      /*
       System.out.println(arr[19][22]);
       System.out.println(arr[22][29]);
       System.out.println(arr[29][1]);
       System.out.println(arr[1][6]);
       System.out.println(arr[6][41]);
       System.out.println(arr[41][20]);
       System.out.println(arr[20][16]);
       System.out.println(arr[16][2]);
       System.out.println(arr[2][44]);
       System.out.println(arr[44][18]);
       System.out.println(arr[18][40]);
       System.out.println(arr[40][7]);
       System.out.println(arr[7][8]);
       System.out.println(arr[8][9]);
       System.out.println(arr[9][42]);
       System.out.println(arr[42][32]);
       System.out.println(arr[32][50]);
       System.out.println(arr[50][10]);
       System.out.println(arr[10][51]);
       System.out.println(arr[51][13]);
       System.out.println(arr[13][12]);
       System.out.println(arr[12][46]);
       System.out.println(arr[46][25]);
       System.out.println(arr[25][26]);
       System.out.println(arr[26][27]);
       System.out.println(arr[27][11]);
       System.out.println(arr[11][24]);
       System.out.println(arr[24][3]);
       System.out.println(arr[3][5]);
       System.out.println(arr[5][14]);
       System.out.println(arr[14][4]);
       System.out.println(arr[4][37]);
       System.out.println(arr[37][23]);
       System.out.println(arr[23][47]);
       System.out.println(arr[47][45]);
       System.out.println(arr[45][36]);
       System.out.println(arr[36][39]);
       System.out.println(arr[39][38]);
       System.out.println(arr[38][33]);
       System.out.println(arr[33][34]);
       System.out.println(arr[34][35]);
       System.out.println(arr[35][48]);
       System.out.println(arr[48][31]);
       System.out.println(arr[31][17]);
       System.out.println(arr[17][30]);
       System.out.println(arr[30][21]);
       System.out.println(arr[21][0]);
       System.out.println(arr[0][43]);
       System.out.println(arr[43][15]);
       System.out.println(arr[15][28]);
       System.out.println(arr[28][49]);
       System.out.println(arr[49][19]);
       
     */
        
    }
    
}
