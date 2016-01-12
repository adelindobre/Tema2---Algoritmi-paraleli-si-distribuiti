/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dobre
 */
public class Tema2 {
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException, Exception {
        int nr_threads;
        RandomAccessFile file_input_name, document;
        BufferedWriter file_output_name;
        long block_size, last_block_size, offset, number_of_blocks;
        int files_number;
        BlockResult bresult; FinalResult fr; Reduce rd;
        String line;
        Iterator it;
        
        int lungime;
        
        nr_threads = Integer.parseInt(args[0]);
        file_input_name = new RandomAccessFile(new File(args[1]), "r");
        file_output_name =new BufferedWriter(new FileWriter(args[2]));
        
        ExecutorService executorService = Executors.newFixedThreadPool(nr_threads); 
        ExecutorService executorService_reduce = Executors.newFixedThreadPool(nr_threads);
        
        Set<Map> callables = new HashSet<Map>();
        Set<Reduce> callables_reduce = new HashSet<Reduce>();
        
        block_size = Long.parseLong(file_input_name.readLine());
        files_number = Integer.parseInt(file_input_name.readLine());
        ArrayList<String> files_names = new ArrayList<String>();
        
        for(int i = 0; i < files_number; i++){
            line = file_input_name.readLine();
            files_names.add(line);
            document = new RandomAccessFile(new File(line), "r");
            number_of_blocks = document.length() / block_size;
            last_block_size = document.length() - number_of_blocks * block_size;
            offset = 0;
            for(int j = 0; j < number_of_blocks; j++){
                callables.add(new Map(line, block_size, offset));
                offset += block_size;
            }
            callables.add(new Map(line, last_block_size, offset));
            callables_reduce.add(new Reduce(line));
            document.close();
        }
        file_input_name.close();
           
        List<Future<BlockResult>> futures = executorService.invokeAll(callables);
        executorService.shutdown();
        for(Future<BlockResult> future : futures){
            bresult = future.get();
            it = callables_reduce.iterator();
            while(it.hasNext()){
                rd = (Reduce)it.next();
                if (rd.file_name.compareTo(bresult.file_name) == 0){
                    callables_reduce.remove(rd);
                    rd.addElement(bresult);
                    callables_reduce.add(rd);
                    break;
                }
            }
        }
        List<Future<FinalResult>> futures_reduce = executorService_reduce.invokeAll(callables_reduce);
        executorService_reduce.shutdown();
        Collections.sort(futures_reduce, new Comparator<Future<FinalResult>>(){

            @Override
            public int compare(Future<FinalResult> o1, Future<FinalResult> o2) {       
                try{
                    if((Math.floor(o1.get().rang * 100) / 100) > (Math.floor(o2.get().rang * 100) / 100))
                        return -1;
                    else if((Math.floor(o1.get().rang * 100) / 100) < (Math.floor(o2.get().rang * 100) / 100))
                        return 1;
                    else
                    {
                        if(files_names.indexOf(o1) < files_names.indexOf(o2))
                            return -1;
                        else
                            return 1;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tema2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(Tema2.class.getName()).log(Level.SEVERE, null, ex);
                }
                return 0;
              }      
        });
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df  = new DecimalFormat("#.##", otherSymbols);
       
        df.setRoundingMode(RoundingMode.DOWN);
        for(Future<FinalResult> future : futures_reduce){
            fr = future.get();
            file_output_name.write(new String(fr.file_name + ";" + df.format(fr.rang) + ";[" + fr.max_dim + "," + fr.max_words_nr + "]"));
            file_output_name.newLine();
        }
        file_output_name.close();
 
    
      
    
    }
    
}
