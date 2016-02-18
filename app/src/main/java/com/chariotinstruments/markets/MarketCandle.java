package com.chariotinstruments.markets;

/**
 * Created by user on 2/17/16.
 */
public class MarketCandle {

    private int _id;
    private int _minute;
    private double _open;
    private double _high;
    private double _low;
    private double _close;
    private long _volume;
    private long _date;
    private int _isOpen;
    private int _isClose;


    //Constructor
    public MarketCandle(){

    }

    //Getters/setters
    public int getId(){
        return _id;
    }

    public void setId(int idIn){
        _id = idIn;
    }

    public int getMinute(){
        return _minute;
    }

    public void setMinute(int minuteIn){
        _minute = minuteIn;
    }

    public double getOpen(){
        return _open;
    }

    public void setOpen(double openIn){
        _open = openIn;
    }

    public double getHigh(){
        return _high;
    }

    public void setHigh(double highIn){
        _high = highIn;
    }

    public double getLow(){
        return _low;
    }

    public void setLow(double lowIn){
        _low = lowIn;
    }

    public double getClose(){
        return _close;
    }

    public void setClose(double closeIn){
        _close = closeIn;
    }

    public Long getVolume(){
        return _volume;
    }

    public void setVolume(Long volumeIn){
        _volume = volumeIn;
    }

    public long getDate(){
        return _date;
    }

    public void setDate(long dateIn){
        _date = dateIn;
    }

    public int getIsOpen(){
        return _isOpen;
    }

    public void setIsOpen(int isOpenIn){
        _isOpen = isOpenIn;
    }

    public int getIsClose(){
        return _isClose;
    }

    public void setIsClose(int isCloseIn){
        _isClose = isCloseIn;
    }


}
