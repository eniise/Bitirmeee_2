package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Icon;

import androidx.annotation.DrawableRes;

public class MyAlertDialog {
    private Context context;
    private String Title;
    private String Message;
    private @DrawableRes int Icon;
    public MyAlertDialog(Context context, String title, String message, @DrawableRes int icon){
        this.context = context;
        this.Title = title;
        this.Message = message;
        this.Icon = icon;
    }
    public AlertDialog.Builder ShowMessage(){
        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(context);
        tempBuilder.setTitle(Title);
        tempBuilder.setMessage(Message);
        tempBuilder.setIcon(Icon);
        return tempBuilder;
    }
}
