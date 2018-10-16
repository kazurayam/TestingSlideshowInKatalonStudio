Testing Slideshow in Katalon Studio
=====

## What is this?

This is a [Katalon Studio](https://www.katalon.com/) project for demonstration purpose. You can clone this out to your PC and execute in Katalon Studio.

This project was developed using Katalon Studio 5.7.1.

This project was developed to propose solutions to the following discussion posted in the [Katalon Forum](https://forum.katalon.com/discussions).

1. [Verify Image Present in slideshow](https://forum.katalon.com/discussion/9985/verify-image-present-in-slideshow-)

----

## Problems to solve

Many web sites have slide show in the top page. For example, have a look at this beautiful site: [Mandelbrot Explorer](https://www.mandel.org.uk/). A slide show displays a fixed number of images switched circularly with a fixed intervals in seconds.

I want to verify *if a slide show is in motion*. In other words,

1. I want to take screenshots of each comprising images of a slide show, and save them into file
2. I want to compare pairs of sibling images (img0, img1), (img1, img2), (img2, img3) and (img3, img0) to find out if each pairing images are different or not.
3. I want to specify a criteria number in pecentage (e.g. 70.0%). If a pair of images have larger difference (e.g. 99.0%) then we judge that the pair is different, otherwise we judge the pair is not different enough.
4. If I could find all of the image pairs: (img0, img1), (img1, img2), (img2, img3) and (img3, img0) are different, then I would conclude that the slide show is in motion.

## Solution

[aShot](https://github.com/yandex-qatools/ashot), WebDriver Screenshot utility, enables you to take a screenshot of a selected WebElement (e.g. `<div id="banner">`). Also aShot enables you to compare 2 images of the WebElement to figure out how much different they are. This Katalon Studio project provides a set of Custom Keywords in Katalon Studio, which wrap the [aShot](https://github.com/yandex-qatools/ashot) API. Also the project provides an example Test Case which shows how to make use of the keywords.

If you run a test which takes screen shots, you would inevitably want to save images into files on your local disk. You will encounter another associated problem: how to mange the paths of generated files. My [Materials](https://github.com/kazurayam/Materials) project provides a smart solution for managing & resolving paths of image files.

## Demonstration

### Input

The demo will open [Mandelbrot Explorer](https://www.mandel.org.uk/) and verify if its slide show in the banner is in motion or not.

### How to run the demo

1. download the ZIP file of this demo project from the [releases] Page
2. unzip it
3. start your Katalon Studio, open the project
4. Open `Test Cases/verify-slideshow-example` and run it. You can choose any browser. You apply the `default` Profile, which is the sole profile provided.
5. Or you open `Test Suites/TS1` and run it.  

### Output

#### Output from `verify-slideshow-example`

Running the `Test Cases/vefiry-slideshow-example` will result a new folder under the project directory: `./tmp/slideshow`.
![tmp/slideshow folder](docs/images/screenshots_in_tmp_slideshow.png)

In the folder you will find 4 screenshots of the original images. These are taken with 6 seconds of interval.
- `Mandelbrot Explorer_0.png` ![`Mandelbrot Explorer_0.png`](docs/images/slideshow/Mandelbrot%20Explorer_0.png)
- `Mandelbrot Explorer_1.png` ![`Mandelbrot Explorer_1.png`](docs/images/slideshow/Mandelbrot%20Explorer_1.png)
- `Mandelbrot Explorer_2.png` ![`Mandelbrot Explorer_2.png`](docs/images/slideshow/Mandelbrot%20Explorer_2.png)
- `Mandelbrot Explorer_3.png` ![`Mandelbrot Explorer_3.png`](docs/images/slideshow/Mandelbrot%20Explorer_3.png)

Also in the same folder you fill find 4 comparison result of the original images:
- img0 and img1 comparison (75.34% of difference)![img0 and img1 comparison](docs/images/slideshow/Mandelbrot%20Explorer_diff_0x1%2875.34%29.png)
- img1 and img2 comparison (99.80% of difference) ![img1 and img2 comparison](docs/images/slideshow/Mandelbrot%20Explorer_diff_1x2%2899.80%29.png)
- img2 and img3 comparison (100% of difference) ![img2 and img3 comparison](docs/images/slideshow/Mandelbrot%20Explorer_diff_2x3%28100.00%29.png)
- img3 and img0 comparison (100% of difference) ![img3 and img0 comparison](docs/images/slideshow/Mandelbrot%20Explorer_diff_3x0%28100.00%29.png)

I set 70.0% as the criteria to determine a image difference is significant or not. And the 4 comparison results has differece of 75.34%, 99.80%, 100.00% and 100.00% --- these are greater than the criteria; 4 images are all different. Therefore the test case PASSES.


### Output from the `Test Suites/TS1`

Running the `Test Suites/TS1` will result a new folder under the project directory: `./Materials/TS1/yyyyMMdd_hhmmss/main.verifySlideshow`.
![tmp/slideshow folder](docs/images/screenshots_in_Materials.png)

Description of the folder path:
- `Materials` : fixed name
- `TS1` : the test suite name
- `yyyyMMdd_hhmmss` : the timestamp when the Test Suite was invoked.
- `main.verifySlideshow`: the test case name, derived from the test case id `Test Cases/main/verifySlideshow`

The image files are just the same as the files created by the test case `verify-slideshow-example` above. 

## Code description

### Custom keywords

### Test Case: verify-slideshow-example

### Test Case: main/verifySlideshow called by Test Suite TS1

## Step by step instruction to recreate your slide show test

## Thanks

Special thanks to [Mandelbrot Explorer](https://www.mandel.org.uk/). I picked up this as an example of web site with slide show. I found it in the page of Drupal Module [Views Slideshow](https://www.drupal.org/project/views_slideshow).
