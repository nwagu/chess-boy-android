package com.nwagu.android.chessboy.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nwagu.android.chessboy.model.data.BluetoothDevice
import com.nwagu.android.chessboy.players.BluetoothPlayer
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.flow.MutableStateFlow

class NewBluetoothGameViewModel(application: Application): AndroidViewModel(application) {

    val selectedColor = MutableStateFlow(ChessPieceColor.WHITE)
    val selectedDevice = MutableStateFlow<BluetoothPlayer?>(null)

    val bluetoothChatService: BluetoothChatService by lazy { BluetoothChatService() }

    val scanState = MutableStateFlow(ScanState.NONE)
    val connectState by lazy {  bluetoothChatService.connectionState }

    var discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(listOf())

    lateinit var onConnectSuccessHandler: (BluetoothChatService) -> Unit

    fun onDeviceFound(device: BluetoothDevice) {
        if (device.address in discoveredDevices.value.map { it.address })
            return

        val newList = mutableListOf<BluetoothDevice>()
        newList.addAll(discoveredDevices.value)
        newList.add(device)
        discoveredDevices.value = newList
    }

    fun onScanFinished() {
        scanState.value = ScanState.SCAN_FINISHED
    }

    fun attemptConnectToDevice(address: String) {
        bluetoothChatService.init(getApplication(), true)
        bluetoothChatService.setListener(bluetoothChatListener)
        bluetoothChatService.connectDevice(address, true)
    }

    fun listenForConnection() {
        bluetoothChatService.init(getApplication(), false)
        bluetoothChatService.setListener(bluetoothChatListener)
        bluetoothChatService.startListeningForConnection()
    }

    val bluetoothChatListener: BluetoothChatService.ChatListener
        get() = object : BluetoothChatService.ChatListener {

            override fun onConnecting() {
                //
            }

            override fun onListening() {
                //
            }

            override fun onConnected(address: String) {
                onConnectSuccessHandler(bluetoothChatService)
            }

            override fun onChatStart(deviceName: String) {
                //
            }

            override fun onConnectionFailed() {
                //
            }

            override fun onMessageSent(message: String) {
                //
            }

            override fun onMessageReceived(message: String) {
                //
            }

            override fun onDisconnected() {
                //
            }
        }

}

enum class ScanState {
    NONE, SCANNING, SCAN_FINISHED
}