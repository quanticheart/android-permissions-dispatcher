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
 *  * Copyright(c) Developed by John Alves at 2020/1/28 at 6:29:35 for quantic heart studios
 *
 */

package com.quanticheart.permissionsdispatcherfuctions.contacts

import android.content.ContentProviderOperation
import android.content.OperationApplicationException
import android.database.Cursor
import android.os.Bundle
import android.os.RemoteException
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.quanticheart.permissionsdispatcherfuctions.R
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlin.properties.Delegates

class ContactsFragment : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contacts)

        back?.setOnClickListener {
            finish()
        }
        contact_add.setOnClickListener {
            insertDummyContact()
        }
        contact_load.setOnClickListener {
            //            loadContact()
        }
    }

    /**
     * Restart the Loader to query the Contacts content provider to display the first contact.
     */
//    private fun loadContact() = this.loaderManager.restartLoader(0, null, this@ContactsFragment)

    /**
     * Initialises a new [CursorLoader] that queries the [ContactsContract].
     */
    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> = CursorLoader(
        this,
        ContactsContract.Contacts.CONTENT_URI,
        PROJECTION,
        null,
        null,
        ORDER
    )

    /**
     * Dislays either the name of the first contact or a message.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursor?.let {
            val totalCount: Int = it.count
            if (totalCount > 0) {
                it.moveToFirst()
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contact_message.text =
                    resources.getString(R.string.contacts_string, totalCount, name)

                Log.d(TAG, "First contact loaded: $name")
                Log.d(TAG, "Total number of contacts: $totalCount")
                Log.d(TAG, "Total number of contacts: $totalCount")
            } else {
                Log.d(TAG, "List of contacts is empty.")
                contact_message.setText(R.string.contacts_empty)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) =
        contact_message.setText(R.string.contacts_empty)

    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     *
     *
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
    private fun insertDummyContact() {
        // Two operations are needed to insert a new contact.
        val operations = arrayListOf<ContentProviderOperation>()

        // First, set up a new raw contact.
        ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build().let { operations.add(it) }


        // Next, set the name for the contact.
        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            .withValue(
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                DUMMY_CONTACT_NAME
            )
            .build().let { operations.add(it) }

        // Apply the operations.
        try {
            contentResolver?.applyBatch(ContactsContract.AUTHORITY, operations)
        } catch (e: RemoteException) {
            Log.d(TAG, "Could not add a new contact: " + e.message)
        } catch (e: OperationApplicationException) {
            Log.d(TAG, "Could not add a new contact: " + e.message)
        }
    }

    companion object {
        private const val TAG = "Contacts"

        private const val DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample"

        /**
         * Projection for the content provider query includes the id and primary name of a contact.
         */
        private val PROJECTION =
            arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        /**
         * Sort order for the query. Sorted by primary name in ascending order.
         */
        private const val ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"

        /**
         * Creates a new instance of a ContactsFragment.
         */
        fun newInstance(): ContactsFragment =
            ContactsFragment()
    }
}