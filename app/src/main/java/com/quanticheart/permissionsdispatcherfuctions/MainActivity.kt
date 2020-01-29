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
 *  * Copyright(c) Developed by John Alves at 2020/1/28 at 6:21:33 for quantic heart studios
 *
 */

package com.quanticheart.permissionsdispatcherfuctions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.permissionsdispatcherfuctions.camera.CameraActivity
import com.quanticheart.permissionsdispatcherfuctions.contacts.ContactsFragment
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

/**
 * https://github.com/permissions-dispatcher/PermissionsDispatcher
 */

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_camera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        button_contacts.setOnClickListener {
            startActivity(Intent(this, ContactsFragment::class.java))
        }
        verifyPermissionWithPermissionCheck()
    }

    @NeedsPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )
    fun verifyPermission() {
    }

    @OnShowRationale(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )
    fun showRationaleForCamera(request: PermissionRequest) {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

//    @NeedsPermission(Manifest.permission.CAMERA)
//    fun showCamera() {
//        // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will have been granted
//        startActivity(Intent(this, CameraPreviewFragment::class.java))
//    }
//
//    @OnPermissionDenied(Manifest.permission.CAMERA)
//    fun onCameraDenied() {
//        // NOTE: Deal with a denied permission, e.g. by showing specific UI
//        // or disabling certain functionality
//        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
//    }
//
//    @OnShowRationale(Manifest.permission.CAMERA)
//    fun showRationaleForCamera(request: PermissionRequest) {
//        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
//        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
//        showRationaleDialog(this, R.string.permission_camera_rationale, request)
//    }
//
//    @OnNeverAskAgain(Manifest.permission.CAMERA)
//    fun onCameraNeverAskAgain() {
//        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show()
//    }
//
//    @NeedsPermission(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
//    fun showContacts() {
//        // NOTE: Perform action that requires the permission.
//        // If this is run by PermissionsDispatcher, the permission will have been granted
//        startActivity(Intent(this, ContactsFragment::class.java))
//    }
//
//    @OnPermissionDenied(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
//    fun onContactsDenied() {
//        // NOTE: Deal with a denied permission, e.g. by showing specific UI
//        // or disabling certain functionality
//        Toast.makeText(this, R.string.permission_contacts_denied, Toast.LENGTH_SHORT).show()
//    }
//
//    @OnShowRationale(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
//    fun showRationaleForContacts(request: PermissionRequest) {
//        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
//        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
//        showRationaleDialog(this, R.string.permission_contacts_rationale, request)
//    }
//
//    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
//    fun onContactsNeverAskAgain() {
//        Toast.makeText(this, R.string.permission_contacts_never_ask_again, Toast.LENGTH_SHORT)
//            .show()
//    }
}