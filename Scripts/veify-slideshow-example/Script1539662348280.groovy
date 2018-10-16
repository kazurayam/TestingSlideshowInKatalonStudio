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

String url = 'https://www.mandel.org.uk/'
String title = 'Mandelbrot Explorer'
int intervalSeconds = 6
int slideCount = 4
Double criteriaPercent = 70.0

// ensure output directory, clean it
Path tmpDir = Paths.get(RunConfiguration.getProjectDir()).resolve('tmp/slideshow')
if (Files.exists(tmpDir)) {
	FileUtils.cleanDirectory(tmpDir.toFile())
}
Files.createDirectories(tmpDir)

// open browser
WebUI.openBrowser('')

WebUI.setViewPortSize(1024,768)

WebUI.navigateToUrl(url)

TestObject bannerTO = findTestObject('Page - Mandelbrot Explorer Home/div_banner')

// make sure the banner is present
WebUI.verifyElementPresent(bannerTO, 10, FailureHandling.STOP_ON_FAILURE)

// Need to wait for the timer starts  ---  it is very difficult to find appropriate value of this 
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
	
	// write the difference image into file
	Path out = tmpDir.resolve("${title}_diff_${i}x${x}(${difference.getRatioAsString()}).png")
	ImageIO.write(difference.getDiffImage(), "PNG", out.toFile())
	
	// verify if different enough
	CustomKeywords.'com.kazurayam.ksbackyard.Assert.assertTrue'(
		"diff_${i}x${x} looks unchanged",
		difference.imagesAreDifferent(),
		FailureHandling.CONTINUE_ON_FAILURE)
}

WebUI.closeBrowser()