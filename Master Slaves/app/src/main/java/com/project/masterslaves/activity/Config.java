package com.project.masterslaves.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Intkonsys on 24/02/2017.
 */

public class Config
{
    static String Main_URL="http://tracking.freeprojecttest.online";

    private static final String EMAIL_REGEX="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    private static Pattern pattern;
    //non-static Matcher object because it's created from the input String
    private static Matcher matcher;

    public static boolean is_email(String in_email)
    {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        String email_parts[]=in_email.split("@");///email_parts[0]=yogesh,email_parts[1]=gmail.com
        if(email_parts.length==2)
        {
            String further_parts[]=email_parts[1].split("\\.");
            if(further_parts.length==2)
            {
                if(further_parts[1].equals("com")||further_parts[1].equals("in")||further_parts[1].equals("org"))
                {
                    matcher = pattern.matcher(in_email);
                    return matcher.matches();
                }


                else
                    return false;
            }
            else
                return false;
        }
        else
            return false;

    }

    public static boolean is_num(String in_number)
    {
        try
        {
            double i=Double.parseDouble(in_number);
            if(in_number.length()==10||in_number.length()==12)
                return true;
            else
                return false;
        }
        catch(Exception e)
        {
            return false;
        }

    }


    public static boolean validateName( String firstName )
    {
        return firstName.matches( "[A-Z][a-zA-Z]*" );
    } // end method validateFirstName
}
