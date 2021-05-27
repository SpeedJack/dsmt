package it.unipi.dsmt.das.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public Utility(){}
    public String convertDate(long diff) {
        if(diff < 60)
            return "< 1 M";
        else if(diff < 3600){
            long min = Math.floorDiv(diff, 60);
            return min + " M";
        }
        else if(diff < 86400){
            long hour = Math.floorDiv(diff, 3600);
            return hour + " H";
        }
        else if(diff < 604800){
            long day = Math.floorDiv(diff, 86400);
            return day + " D";
        }
        else
            return "> 1 W";
    }

    public String getDate(long timestamp){
        Date date=new Date(timestamp*1000);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return df2.format(date);
    }

    public long getTimestamp(String timestamp){
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return Math.floorDiv(df.parse(timestamp).getTime(),1000);
        }
        catch (ParseException e) {
            return 0;
        }
    }
}
