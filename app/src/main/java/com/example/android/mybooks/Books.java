package com.example.android.mybooks;

/**
 * Created by ANGA KOKO on 7/28/2017.
 */

public class Books {

    String mTitle;
    String mDescription;
    String mAuthor;
    String mYear;
    String mUrl;
    String mImageLinkSmall;
    String mImageLinkLarge;
    String mSubTitle;

    public Books(String title, String subTitle, String description, String author, String year, String url, String imageLinkSmall, String imageLinkLarge){
        mTitle = title;
        mDescription = description;
        mAuthor = author;
        mYear = year;
        mUrl = url;
        mImageLinkSmall = imageLinkSmall;
        mImageLinkLarge = imageLinkLarge;
        mSubTitle = subTitle;
    }

    /**Public Method to get Title of Book */
    public String getTitle(){return mTitle;}

    /**Public Method to get Sub Title of Book */
    public String getSubTitle(){return mSubTitle;}

    /**Public Method to get Description of Book */
    public String getDescription(){return mDescription;}

    /**Public Method to get Author of Book */
    public String getAuthor(){return mAuthor;}

    /**Public Method to get Year Published of Book */
    public String getYear(){return mYear;}

    /**Public Method to get URL of Book */
    public String getUrl(){return mUrl;}

    /**Public Method to get Image Link of Book */
    public String getImageLinkSmall(){return mImageLinkSmall;}

    /**Public Method to get Large Image Link of Book */
    public String getImageLinkLarge(){return mImageLinkLarge;}

}
