package excel2database;

import java.util.ArrayList;
public class ToolsUtils {
    public static ArrayList<String> SelectMax(ArrayList<String> data, int[] col){
        int max = 0;
        int max_index = 0;
        int[] feature = new int[]{0,0,0,0,0,0,0,0,0,0};
        //选出最大值
        for (int i=0;i<col.length;i++){
            if (max<Integer.valueOf(data.get(col[i]))){
                max = Integer.valueOf(data.get(col[i]));
                max_index = i;
            }
        }
        data.add(String.valueOf(max));
        for(int i = 0;i<col.length;i++){
            if (i == 0){
                if (Integer.valueOf(data.get(col[i]))>Integer.valueOf(data.get(col[i+1])))
                feature[i] = 1;
            }else{
                if (Integer.valueOf(data.get(col[i-1]))==0){
                    if (Integer.valueOf(data.get(col[i]))>10)
                        feature[i]=1;
                }else if (Integer.valueOf(data.get(col[i]))>Integer.valueOf(data.get(col[i-1]))*2)
                    feature[i]=1;
            }
        }
        for (int i : feature)
            data.add(String.valueOf(i));
        if (match(feature))data.add("1");
        else data.add("0");
        return data;
    }

    public static boolean match(int[] data){
        int sum = 0;
        for (int i : data){
            sum+= i;
        }
        if (sum>=2)
            return true;
        return  false;
    }
    public static boolean allzero(int[] data){
        int j = 0;
        for (int i: data){
            if (i>0)j++;
        }
        if (j>0){
            return  false;
        }
        else{
            return true;
        }
    }
}