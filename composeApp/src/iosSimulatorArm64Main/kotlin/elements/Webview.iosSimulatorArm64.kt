package elements

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.CValue
import platform.WebKit.WKWebView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRect
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewElement(modifier: Modifier, html: String) {
    val url = remember { html }
    val webview = remember { WKWebView() }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val container = UIView()
            webview.apply {
                WKWebViewConfiguration().apply {
                    allowsInlineMediaPlayback = true
                    allowsAirPlayForMediaPlayback = true
                    allowsPictureInPictureMediaPlayback = true
                }
                loadHTMLString(url, null)
//                loadRequest(request = NSURLRequest(NSURL(string = url)))
            }
            container.addSubview(webview)
            container
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            webview.setFrame(rect)
            CATransaction.commit()
        })
}