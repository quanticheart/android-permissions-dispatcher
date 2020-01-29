/*
 *
 *  *                                     /@
 *  *                      __        __   /\/
 *  *                     /==\      /  \_/\/
 *  *                   /======\    \/\__ \__
 *  *                 /==/\  /\==\    /\_|__ \
 *  *              /==/    ||    \=\ / / / /_/
 *  *            /=/    /\ || /\   \=\/ /
 *  *         /===/   /   \||/   \   \===\
 *  *       /===/   /_________________ \===\
 *  *    /====/   / |                /  \====\
 *  *  /====/   /   |  _________    /      \===\
 *  *  /==/   /     | /   /  \ / / /         /===/
 *  * |===| /       |/   /____/ / /         /===/
 *  *  \==\             /\   / / /          /===/
 *  *  \===\__    \    /  \ / / /   /      /===/   ____                    __  _         __  __                __
 *  *    \==\ \    \\ /____/   /_\ //     /===/   / __ \__  ______  ____ _/ /_(_)____   / / / /__  ____ ______/ /_
 *  *    \===\ \   \\\\\\\/   ///////     /===/  / / / / / / / __ \/ __ `/ __/ / ___/  / /_/ / _ \/ __ `/ ___/ __/
 *  *      \==\/     \\\\/ / //////       /==/  / /_/ / /_/ / / / / /_/ / /_/ / /__   / __  /  __/ /_/ / /  / /_
 *  *      \==\     _ \\/ / /////        |==/   \___\_\__,_/_/ /_/\__,_/\__/_/\___/  /_/ /_/\___/\__,_/_/   \__/
 *  *        \==\  / \ / / ///          /===/
 *  *        \==\ /   / / /________/    /==/
 *  *          \==\  /               | /==/
 *  *          \=\  /________________|/=/
 *  *            \==\     _____     /==/
 *  *           / \===\   \   /   /===/
 *  *          / / /\===\  \_/  /===/
 *  *         / / /   \====\ /====/
 *  *        / / /      \===|===/
 *  *        |/_/         \===/
 *  *                       =
 *  *
 *  * Copyright(c) Developed by John Alves at 2020/1/28 at 6:29:20 for quantic heart studios
 *
 */

package com.quanticheart.permissionsdispatcherfuctions.camera

import android.Manifest
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.permissionsdispatcherfuctions.R
import com.quanticheart.permissionsdispatcherfuctions.dialogs.DialogsProject.showRationaleDialog
import kotlinx.android.synthetic.main.fragment_camera.*
import permissions.dispatcher.*

@RuntimePermissions
class CameraActivity : AppCompatActivity() {

    private var preview: CameraPreview? = null
    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_camera)
        back?.setOnClickListener {
            finish()
        }
        showCameraWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
        initCamera()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
        finish()
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(this, R.string.permission_camera_rationale, request)
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() {
        finish()
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun initCamera() {
        camera = getCameraInstance(
            CAMERA_ID
        )?.also { camera ->
            val cameraInfo = Camera.CameraInfo().also { Camera.getCameraInfo(CAMERA_ID, it) }
            // Get the rotation of the screen to adjust the preview image accordingly.
            val displayRotation = windowManager?.defaultDisplay?.rotation
            camera_preview?.removeAllViews()
            if (displayRotation == null) {
                return
            }
            preview?.setCamera(camera, cameraInfo, displayRotation) ?: run {
                // Create the Preview view and set it as the content of this Activity.

                preview =
                    CameraPreview(
                        this,
                        camera,
                        cameraInfo,
                        displayRotation
                    )
            }
            camera_preview?.addView(preview)
        }
    }

    override fun onResume() {
        super.onResume()
        camera ?: initCamera()
    }

    override fun onPause() {
        super.onPause()
        // Stop camera access
        releaseCamera()
    }

    private fun releaseCamera() {
        camera?.release()?.run { camera = null }
        // release destroyed preview
        preview = null
    }

    companion object {
        private const val TAG = "CameraPreview"

        /**
         * Id of the camera to access. 0 is the first camera.
         */
        private const val CAMERA_ID = 0

        fun newInstance(): CameraActivity =
            CameraActivity()

        /** A safe way to get an instance of the Camera object.  */
        fun getCameraInstance(cameraId: Int): Camera? = try {
            // attempt to get a Camera instance
            Camera.open(cameraId)
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Camera " + cameraId + " is not available: " + e.message)
            null
        }
    }
}