import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import javax.imageio.ImageIO

import org.apache.commons.io.FileUtils

import com.kazurayam.ksbackyard.ScreenshotDriver.ImageDifference
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/*
 * verify-slideshow-example
 */

// declaring parameters
String url = 'https://www.mandel.org.uk/'
String title = 'Mandelbrot Explorer'        // will be used as file name prefix
int intervalSeconds = 6                     // target slideshow changes every N secodns
int slideCount = 4                          // target slideshow consists of N images
Double criteriaPercent = 70.0               // if the difference of 2 images are greater than this, they are judged to be different

// ensure output directory, clean it
Path tmpDir = Paths.get(RunConfiguration.getProjectDir()).resolve('tmp/slideshow')
if (Files.exists(tmpDir)) {
	FileUtils.cleanDirectory(tmpDir.toFile())
}
Files.createDirectories(tmpDir)

// open browser
WebUI.openBrowser('')

// smaller window is prefered
WebUI.setViewPortSize(1024,768)

// open the target page
WebUI.navigateToUrl(url)

TestObject bannerTO = findTestObject('Page - Mandelbrot Explorer Home/div_banner')

// make sure the banner is present in the page shown on the browser window
WebUI.verifyElementPresent(bannerTO, 10, FailureHandling.STOP_ON_FAILURE)

// Need to wait for the timing when the interval starts
//  ---  it was a bit difficult to find appropriate how long we should wait 
WebUI.delay(3)

// take screenshots of slides and save it to disk
List<BufferedImage> imageList = new ArrayList<BufferedImage>()
for (int i = 0; i < slideCount; i++) {
	BufferedImage img = CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeElementImage'(
		bannerTO)
	Path out = tmpDir.resolve("${title}_${i}.png")
	ImageIO.write(img, "PNG", out.toFile())
	imageList.add(img)
	// wait for the slide to change
	WebUI.delay(intervalSeconds)
}

// check differences of the image pairs:
//     (img0,img1), (img1,img2), (img2,img3) ... (imgN-1, imgN), (imgN, img0)
// save the diff-images to disk,
List<ImageDifference> diffList = new ArrayList<ImageDifference>()
for (int i = 0; i < slideCount; i++) {
	int x = (i < slideCount - 1) ? (i + 1) : 0
	
	// compare 2 images
	ImageDifference difference =
		CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.verifyImages'(
			imageList.get(i), imageList.get(x), criteriaPercent)
	diffList.add(difference)
	
	// write the difference-image into file
	Path out = tmpDir.resolve("${title}_diff_${i}x${x}(${difference.getRatioAsString()}).png")
	ImageIO.write(difference.getDiffImage(), "PNG", out.toFile())
	
	// verify if the pair is different enough
	CustomKeywords.'com.kazurayam.ksbackyard.Assert.assertTrue'(
		"diff_${i}x${x} looks unchanged",
		difference.imagesAreDifferent(),
		FailureHandling.CONTINUE_ON_FAILURE)
}

// finito
WebUI.closeBrowser()