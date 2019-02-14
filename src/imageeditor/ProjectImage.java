 /* ProjectImage.java
 *  Modified by Michael Xiao, 02/02/19
 *  Part of ImageEditor project - perform various operations on an image represented
 *  as a 2-dimensional array of pixel values.  Completion of the methods of this 
 *  class constitutes CPS122 Project 1
 *
 * Copyright (c) 2003, 2004, 2005, 2008, 2009, 2013 - Russell C. Bjork
 *
 */

package imageeditor;

import java.io.*;
import java.awt.image.ColorModel;
import java.util.Random;

public class ProjectImage
{
    /** Constructor
     *
     *  @param colorModel the color model to use for interpreting the
     *         pixel values
     *  @param pixels the data content of this image - a two-dimensional array
     *      having height rows, each containing width values.  If this is a
     *      grayscale image, then each element lies in the range 0 .. 255.  If
     *      this is a color image, then each element is a packed 24 bit color
     *      with alpha value
     */
    public ProjectImage(ColorModel colorModel, int [] [] pixels)
    { 
        this.colorModel = colorModel;
        this.pixels = pixels;
        this.height = pixels.length;
        this.width = pixels[0].length;
    }
    
    /**************************************************************************
     * Accessor for information about this image
     *************************************************************************/
     
    /** Find out if this class is capable of handling color images.  This method
     *  is made static so it can be called before an object of this class is
     *  created.
     *
     *  @return true if this class can handle color images.
     *
     *  If the return value is false, this class will assume that images are
     *  represented by grayscale values in the range 0 .. 255.  If the return
     *  value is true, this class can also handle images that are represented by
     *  packed color values, to be interpreted by colorModel.
     */
    public static boolean isColorCapable()
    {
        return false;
    }
    
    /**************************************************************************
     * Accessors for information about this image
     *************************************************************************/
     
    /** Get the pixels
     *
     *  @return the pixels for this image - represented as a 2 dimensional
     *          array of integers, to be interpreted according to the
     *          color model
     */
    public int [] [] getPixels()
    {
        return pixels;
    }
    
    /** Get the pixels of this image as a one-dimensional array of packed RGB
     *  values in the standard representation used internally by the Java
     *  image routines
     *
     *  @return the pixels of this image - represented as a 1 dimensional
     *          array of integers representing packed RGB values
     */
    public int [] getPixelsIntRGB()
    {
        int [] result = new int[height * width];
        for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
                result[row * width + col] = isColor()
                  ? pixels[row][col]
                  : pixels[row][col] * 0x10101; // Makes all three colors same
        return result;
    }

    /** Get the width of this image
     *
     *  @return the width of this image
     */
    public int getWidth()
    {
        return width;
    }
    
    /** Get the height of this image
     *
     *  @return the height of this image
     */
    public int getHeight()
    {
        return height;
    }
    
    /** Get the color model used by this image
     *
     *  @return the color model for this image
     */
    public ColorModel getColorModel()
    {
        return colorModel;
    }
    
    /** Check to see whether this image is color
     * 
     *  @return true if this image is color
     */
    public boolean isColor()
    {
        return ! (colorModel instanceof GrayScaleColorModel);
    }

    /**************************************************************************
     * Mutators to alter this image.  Some of these will alter the
     * image "in place", while others will change the width and/or height,
     * resulting in the creation of a new array of pixels.
     *************************************************************************/
    

    
    
    /** Lighten the image
     */
    public void lighten()
    {
        for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
            {
                pixels[row][col] += LIGHTEN_DARKEN_AMOUNT;
                if (pixels[row][col] > MAX_BRIGHTNESS)
                    pixels[row][col] = MAX_BRIGHTNESS;
            }
    }
    
    
    
    
    /** Darken the image
     */
    public void darken()
    {
        for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
            {
                pixels[row][col] -= LIGHTEN_DARKEN_AMOUNT;
                if (pixels[row][col] < MIN_BRIGHTNESS)
                    pixels[row][col] = MIN_BRIGHTNESS;
            }
    }
    
    
    
    
    /** Get the negative of the image
     */
    public void negative()
    {
        for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
            {
                pixels[row][col] = MAX_BRIGHTNESS - pixels[row][col];
            }
    }
    
    
    
    
    /**Decrease the contrast of an image
     */
    public void reduceContrast()
    {
    int totalColor;
    totalColor = 0;
    
    //First nested for-loop to find the total and average color of the image
    
    for (int row = 0; row < height; row ++)
        for (int col = 0; col < width; col ++)
            totalColor += pixels[row][col];
            int averageColor;
            averageColor = totalColor/(width*height);
            
    //A second nested for-loop to actually compare color values and contrast
    
    for (int row = 0; row < height; row ++)
        for (int col = 0; col < width; col ++)
        {
            if (pixels[row][col] > averageColor)
                pixels[row][col] -= LIGHTEN_DARKEN_AMOUNT;
            else if (pixels[row][col] < averageColor)
                pixels[row][col] += LIGHTEN_DARKEN_AMOUNT; 
            }
    }
    
    
    
    
    /**Increase the contrast of an image              
     */
    public void enhanceContrast()
    {
    int totalColor;
    totalColor = 0;
    for (int row = 0; row < height; row ++)
        for (int col = 0; col < width; col ++)
            totalColor += pixels[row][col];
            int averageColor;
            averageColor = totalColor/(width*height);
    for (int row = 0; row < height; row ++)
        for (int col = 0; col < width; col ++)
        {
            if (pixels[row][col] > averageColor)
                pixels[row][col] += LIGHTEN_DARKEN_AMOUNT;
            if (pixels[row][col] < averageColor)
                pixels[row][col] -= LIGHTEN_DARKEN_AMOUNT;
            
            // Fixes super bright/super dark colors to make sure they don't
            // become negative color values (black -> white & vice versa)
            
            // Used 4 if statements because it is possible that more than one
            // if statement is used
            if (pixels[row][col] < MIN_BRIGHTNESS)
                pixels[row][col] = MIN_BRIGHTNESS;
            if (pixels[row][col] > MAX_BRIGHTNESS)
                pixels[row][col] = MAX_BRIGHTNESS;
            }
    }
   
    
    
    
    /** Flips the image horizontally
     */
    public void flipHorizontally()
    {
    for (int row = 0; row < height; row ++)
        for (int col = 0; col < width/2; col ++)
        {
            //Storing the 2 color values in variables colorFirst and colorSecond
            //This makes switching the colors much much easier
            int colorFirst;
            int colorSecond;
            colorFirst = pixels[row][col];
            colorSecond = pixels[row][width-col-1];
            pixels[row][width-col-1] = colorFirst;
            pixels[row][col] = colorSecond;
        }
    }
    
    
    
    
    /** Flips the image horizontally
     */
    public void flipVertically()
    {
    for (int col = 0; col < width; col ++)
        for (int row = 0; row < height/2; row ++)
        {
            int colorFirst;
            int colorSecond;
            colorFirst = pixels[row][col];
            colorSecond = pixels[height-row-1][col];
            pixels[height-row-1][col] = colorFirst;
            pixels[row][col] = colorSecond;
        }
    }
    
    
    
    
    /**Encrypts and decrypts an image given a key (param is seed)
     *
    * @param seed is a positive integer inputed into the "Key" prompt
    * It is the secret key that will be used to successfully encode and
    * decode the image
    */
    // The following was taken from Stack Overflow:
    // Random random = new Random (seed)
    public void encryptDecrypt(int seed)
    {
        Random random = new Random(seed);
	for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
            {
                // I made a semi-random number/variable that allows
                // the picture to be encrypted while making it possible to
                // be decrypted (randomNumber)
                
                int randomNumber;
                randomNumber = random.nextInt(MAX_BRIGHTNESS + 1);
                pixels[row][col] = pixels[row][col] ^ seed + randomNumber;
            }
    }
    
    
    
    
    /** Calculate the histogram  for the image
    *
    * @return 256-element array. with each element representing a count
    * the number of pixels in the image having that particular brightness
    */
    public int [] calculateHistogram()
    {
        // We need to build a 1-dimensional array to keep track
        // of the number of times each color appears in the image
        
	int [] pixelNumber = new int [256];
	for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
            {
                pixelNumber[pixels[row][col]] += 1;
            }
        
        // Return the array for the histogram button to display
        
        return pixelNumber;
    }
	
    
    
    
    /** Scale the image by a factor of 0.5 in each dimension
     */
    public void halve()
    {
        // We need to build a new image in a separate array, and then make
        // it our current image
        
        int newWidth = width / 2;
        int newHeight = height / 2;
        int [] [] newPixels = new int [newHeight] [newWidth];
        
        // Each pixel in the new image is an average of a 2 x 2 square of pixels
        // in the original image
        
        for (int row = 0; row < newHeight; row ++)
            for (int col = 0; col < newWidth; col ++)
              newPixels[row][col] = (pixels[2*row][2*col] + pixels[2*row+1][2*col] +
                                     pixels[2*row][2*col+1] + pixels[2*row+1][2*col+1])/4;
            
        // Now replace the current image with the one we just created
        
        width = newWidth;
        height = newHeight;
        pixels = newPixels;
    }


    
    
    /**Shifts the image left and right horizontally
    *
    * @param x is an integer (-1 or 1) that determines whether or not the
    * image will shift left or shift right.
    * A noticeable amount of this function is built around parameter x
    * since this one function powers two different buttons
    */
    public void shiftHorizontally(int x)
    {
        
    // I created variables that define the bounds of the for-loops based on
    // whether or not the parameter is equal to -1 or 1
        
    int startColumn;
    int endColumn;
    if (x == -1)
    {
        startColumn = 0;
        endColumn = width - 1;
    }
    else
    {
        startColumn = 1;
        endColumn = width;
    }
    
    int [] [] newPixels = new int [height] [width];
    for (int row = 0; row < height; row ++)
        for (int col = startColumn; col < endColumn; col ++)
        {
            // Each if/else statement appropriately executes a directional shift
            // depending on whether or not x == -1 or 1
            
            if (x == 1)
            {
                newPixels[row][col] = pixels[row][col-1];
                if (col == width - 1)
                    newPixels[row][0] = pixels[row][width-1];
            }
            else
            {
                newPixels[row][col] = pixels[row][col+1];
                if (col == width - 2)
                    newPixels[row][width-1] = pixels[row][0];

            }
        }
    
    // Set the old pixels equal to the newPixels array we just created
    // newPixels is the shifted image we just created
    
    pixels = newPixels;
    }        
    
    
    
    
    /**Shifts the image up and down vertically
    *
    * @param y is an integer (-1 or 1) that determines whether or not the
    * image will shift up or shift down.
    * A noticeable amount of this function is built around parameter y
    * since this one function powers two different buttons
    */
    public void shiftVertically(int y)
    {
    int startRow;
    int endRow;
    if (y == -1)
    {
        startRow = 0;
        endRow = height - 1;
    }
    else
    {
        startRow = 1;
        endRow = height;
    }
    int [] [] newPixels = new int [height] [width];
    for (int col = 0; col < width; col ++)
        for (int row = startRow; row < endRow; row ++)
        {
            if (y == 1)
            {
                newPixels[row][col] = pixels[row-1][col];
                if (row == height - 1)
                    newPixels[0][col] = pixels[height-1][col];
            }
            else
            {
                newPixels[row][col] = pixels[row+1][col];
                if (row == height - 2)
                    newPixels[height-1][col] = pixels[0][col];
            }
        } 
   pixels = newPixels;
    }        
    
    
    
    
    /** Rotates the image 90 degrees clockwise
     */
    public void rotate()
    {
        int newWidth = height;
        int newHeight = width;
        int [] [] newPixels = new int [newHeight] [newWidth];
        for (int row = 0; row < newHeight; row ++)
            for (int col = 0; col < newWidth; col ++)
            {
                newPixels[row][col] = pixels[newWidth-1-col][row];
            }
        
        width = newWidth;
        height = newHeight;
        pixels = newPixels;
    }
    
    
    
    
    /** Scale the image by a factor of 2 in each dimension
     */
    public void doubleSize()
    {
        int newWidth = width * 2 - 1;
        int newHeight = height * 2 - 1;
        int [] [] newPixels = new int [newHeight] [newWidth];
        
        for (int row = 0; row < newHeight; row ++)
            for (int col = 0; col < newWidth; col ++)
            {
                // The doubleSize() algorithm can be expressed using
                // modulus operators and some if statements
                
                if (row%2 == 0 && col%2 == 0)
                    newPixels[row][col] = pixels[row/2][col/2];
                else if (row%2 == 0 && col%2 == 1)
                    newPixels[row][col] = (pixels[row/2][(col-1)/2] + pixels[row/2][(col+1)/2])/2;
                else if (row%2 == 1 && col%2 == 0)
                    newPixels[row][col] = (pixels[(row-1)/2][col/2] + pixels[(row+1)/2][col/2])/2;
                else if (row%2 == 1 && col%2 == 1)
                    newPixels[row][col] = (pixels[(row-1)/2][(col-1)/2] + pixels[(row+1)/2][(col+1)/2])/2;
            }
        
        width = newWidth;
        height = newHeight;
        pixels = newPixels;
    }
    
    
    
    
    /** Apply a filter to this image
    * @param filter a square array of doubles specifying the filter to
    * apply - the number of rows and columns must be odd
    */
    public void applyFilter(double [][] filter)
    {
        int [] [] newPixels = new int [height][width];
        int size = filter.length;
        for (int row = 0; row < height; row ++)
            for (int col = 0; col < width; col ++)
            {
                // The row and column for-loop bounds can be expressed in terms
                // of the size of the filter array
                
                // The following if statement makes sure that the image borders
                // are not filtered due to a lack of neighboring pixels
                
                if (row <= (size-3)/2 || col <= (size-3)/2
                    || row >= height-(size-1)/2 ||col >= width-(size-1)/2)
                {
                    newPixels[row][col] = pixels[row][col];
                }
                
                // The following else if statement does the filtering on all
                // of the remaining non-border pixels
                
                else if (row > (size-3)/2 && row < height-(size-1)/2
                         && col > (size-3)/2 && col < width-(size-1)/2)
                {
                    // double val finds the filtered pixel value
                    double val = 0;
                    
                    // Idea of a second nested for-loop from Betzy;
                    // Expression to find double val was developed by myself
                    for (int y = 0; y < size; y ++)
                        for (int x = 0; x < size; x ++)
                            {
                                val += filter[y][x] * pixels[row + y - ((size-1)/2)]
                                                            [col + x - ((size-1)/2)]; 
                            }

                    // Casting val as an int variable for convenience
                    int integerVal = (int)val;
                    
                    //Color fix
                    //Corrects all "sharpen" colors to what they should be
                    if  (integerVal < MIN_BRIGHTNESS)
                        integerVal = MIN_BRIGHTNESS;
                    else if (integerVal > MAX_BRIGHTNESS)
                        integerVal = MAX_BRIGHTNESS;
                    newPixels[row][col] = integerVal;
                    
                } 
            }
        pixels = newPixels;
    }
    
    
    /* *************************************************************************
     * Utility methods for working with colorized images
     * ************************************************************************/
     
    /** If we are using a full color representation of the image, then pack 
     *  three colors into a single pixel, along with a 100% alpha value.
     *  If any color value is greater than the maximum or less than the minimum,
     *  it is forced to the maximum or minimum value (as the case may be)
     *  before packing.  
     *
     *  @param red the red value to pack
     *  @param green the green value to pack
     *  @param blue the blue value to pack
     *  @return the packed RGB representation for this pixel
     */
    private int pack(int red, int green, int blue)
    {
        if (red > MAX_BRIGHTNESS)
            red = MAX_BRIGHTNESS;
        else if (red < MIN_BRIGHTNESS)
            red = MIN_BRIGHTNESS;
            
        if (green > MAX_BRIGHTNESS)
            green = MAX_BRIGHTNESS;
        else if (green < MIN_BRIGHTNESS)
            green = MIN_BRIGHTNESS;
            
        if (blue > MAX_BRIGHTNESS)
            blue = MAX_BRIGHTNESS;
        else if (blue < MIN_BRIGHTNESS)
            blue = MIN_BRIGHTNESS;
            
        // If we are using a simple 8 bit gray-scale representation for the 
        // image, then all colors are the same and we can simply return any one
        // of them.  Otherwise, we must let the color model pack them correctly
        
        if (colorModel instanceof GrayScaleColorModel)
            return red;
        else
        {
            int [] components = { red, green, blue, 255 };
            return colorModel.getDataElement(components, 0);
        }
    }
    
    /** Average two pixels
     *
     *  @param pixel1 the first of the two pixels
     *  @param pixel2 the second of the two pixels
     *  @return a pixel which is equal to their average
     */
    private int averagePixels(int pixel1, int pixel2)
    {
        int red = (colorModel.getRed(pixel1) + colorModel.getRed(pixel2)) / 2;
        int green = (colorModel.getGreen(pixel1) + colorModel.getGreen(pixel2)) / 2;
        int blue = (colorModel.getBlue(pixel1) + colorModel.getBlue(pixel2)) / 2;
        
        return pack(red, green, blue);
    }
        
            
    
    // Image data
    
    private ColorModel colorModel;
    private int [] [] pixels;
    private int width;
    private int height;
    
    // Constants
    
    private static final int LIGHTEN_DARKEN_AMOUNT = 3;
    private static final int MAX_BRIGHTNESS = 255;
    private static final int MIN_BRIGHTNESS = 0;
}
    
