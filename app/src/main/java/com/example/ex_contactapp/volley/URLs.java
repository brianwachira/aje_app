package com.example.ex_contactapp.volley;

public class URLs {
    private static final String ROOT_URL = "https://aje-api.000webhostapp.com/endpoints.php?apicall=";
    public static final String URL_LOGIN= ROOT_URL + "login"; //'userId', 'givenName'
    public static final String URL_CREATECONTACTGROUP = ROOT_URL + "createcontactgroup"; //'id','groupname','numofcontacts','userid'
    public static final String URL_CREATEGROUPLIST = ROOT_URL + "creategrouplist"; //'contactid','firstname','lastname','phonenumber','groupid','middlename'
    public static final String URL_INSERTMESSAGE = ROOT_URL + "insertmessage"; //'messageid','messagecontent','groupid'

}
