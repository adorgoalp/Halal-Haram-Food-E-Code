package com.adorgolap.ecode.helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.adorgolap.lcs.LCS;

import java.util.ArrayList;

public class Utils {
    public static Bitmap BACK_GROUND = null;
    public static int SCREEN_WIDTH = 0, SCREEN_HEIGHT = 0;
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth,int reqHeight) {
        if(BACK_GROUND ==null) {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            BACK_GROUND = BitmapFactory.decodeResource(res, resId, options);
        }
		return BACK_GROUND;
	}

	public static ArrayList<ECodeData> filterData(ArrayList<ECodeData> originalCopy,
                                                  ArrayList<ECodeData> lowerCaseData,
                                                  String stringToMatch)
	{
        ArrayList<ECodeData> filteredData = new ArrayList<ECodeData>();
        ECodeData singelData;
        ArrayList<Integer> highMatched = new ArrayList<Integer>();
        ArrayList<Integer> mediumMatched = new ArrayList<Integer>();
        ArrayList<Integer> lowMatched = new ArrayList<Integer>();
        for(int i = 0 ; i < lowerCaseData.size() ; i++)
        {
            singelData = lowerCaseData.get(i);
            //scope of efficiency by modyfying the lib LCS. ie return the length directly[Done]
            int lengthMatchedInCode = LCS.getLengthLCS(singelData.code,stringToMatch);
            int lengthMatchedInName = LCS.getLengthLCS(singelData.name,stringToMatch);

            int stringLength = stringToMatch.length();
            if(lengthMatchedInCode == stringLength|| lengthMatchedInName == stringLength)
            {
                highMatched.add(i);
//                filteredData.add(originalCopy.get(i));
            }else if(lengthMatchedInCode == stringLength-1 || lengthMatchedInName == stringLength-1)
            {
                if(!highMatched.contains(i)) {
                    mediumMatched.add(i);
                }
            }
            else if(lengthMatchedInCode == stringLength-2 || lengthMatchedInName == stringLength-2)
            {
                if(!highMatched.contains(i) || !mediumMatched.contains(i)) {
                    lowMatched.add(i);
                }
            }
        }
        highMatched.addAll(mediumMatched);
        highMatched.addAll(lowMatched);
        for(int i : highMatched)
        {
            filteredData.add(originalCopy.get(i));
        }
        return  filteredData;
	}
}
