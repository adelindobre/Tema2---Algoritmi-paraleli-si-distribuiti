/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.Callable;
/**
 *
 * @author Dobre
 */
public class Map implements Callable<BlockResult> {
    
    String file_name;
    RandomAccessFile file_input;
    long offset, block_size;
    
    public Map(String file_name, long block_size, long offset) throws FileNotFoundException{
        this.file_name = file_name;
        this.block_size = block_size;
        this.offset = offset;
    }
    
    //@Override
    public BlockResult call() throws Exception {
        
        file_input = new RandomAccessFile(new File(file_name), "r");
        BlockResult br = new BlockResult();
        br.file_name = file_name;
        char c; int start, contor, max_length = 0;
        byte[] chunk = new byte[(int)block_size];
        String del = ";:/?~\\.,><~`[]{}()_!-@#$%^&+\'=*\"| \t\r\n";
        if(offset != 0){
            file_input.seek(offset - 1);
            c = (char)file_input.read();
                if (del.indexOf(c) >= 0)
                    start = 0;
                else
                    start = 1;
        }else{
            start = 0;
            file_input.seek(offset);
        }
        file_input.readFully(chunk);
        StringBuffer sb = new StringBuffer(new String(chunk));

        if((offset + block_size) < file_input.length()){
            c = (char)file_input.read();          
            while(del.indexOf(c) == -1)
            {
                sb.append(c);
                c = (char)file_input.read();
            }
        }
  
        StringTokenizer str = new StringTokenizer(sb.toString(), del);
        String strs;
        if(str.hasMoreTokens()){
            strs = str.nextToken();
            if(start == 0){
                br.hs.put(strs.length(), 1);
                max_length = strs.length();
                br.max_words.add(strs);
            }
        }
        while(str.hasMoreTokens()){
            strs = str.nextToken();    
            if(!br.hs.containsKey(strs.length())){
               br.hs.put(strs.length(), 1);
            }else{
                contor = (int)br.hs.get(strs.length());
                contor++;
                br.hs.replace(strs.length(), br.hs.get(strs.length()), contor);               
            }
            if(strs.length() > max_length){
                br.max_words.clear();
                max_length = strs.length();
                br.max_words.add(strs);
            } else
                if(strs.length() == max_length){
                    br.max_words.add(strs);
            }
        }
        file_input.close();
        return br;
    }
    
}
