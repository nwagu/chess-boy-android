package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.board.Board
import com.nwagu.chess.convention.getFen
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.moves.*
import jwtc.chess.BoardConstants
import jwtc.chess.JNI
import jwtc.chess.Move.*
import kotlinx.coroutines.flow.MutableStateFlow

actual class JWTC: UCIChessEngine() {

    override val id: String
        get() {
            return "${PlayersRegister.JWTC}-level=${level}"
        }

    override val name = "JWTC"
    override val minLevel = 1
    override val maxLevel = 7
    override var level = 5
    override val connectionState = MutableStateFlow(true)

    lateinit var jni: JNI

    override fun init() {
        jni = JNI()
    }

    override suspend fun getNextMove(board: Board): Move? {

        jni.newGame()
        jni.initFEN(board.getFen())
        jni.searchDepth(level)

        val move = jni.move

        if (move == 0)
            return null

        val source: Int = getFrom(move)
        val destination: Int = getTo(move)

        if (isPromotionMove(move)) {
            val promotionPieceType = when (getPromotionPiece(move)) {
                BoardConstants.QUEEN -> ChessPieceType.QUEEN
                BoardConstants.KNIGHT -> ChessPieceType.KNIGHT
                BoardConstants.ROOK -> ChessPieceType.ROOK
                BoardConstants.BISHOP -> ChessPieceType.BISHOP
                else -> throw IllegalStateException("Invalid Promotion Piece!")
            }
            return Promotion(source, destination, promotionPieceType)
        }

        if (isEP(move)) {
            return EnPassant(source, destination)
        }

        if (isOOO(move)) {
            return Castling(source, destination)
        }

        if (isOO(move)) {
            return Castling(source, destination)
        }

        return RegularMove(source, destination)

    }

    override fun quit() {
        jni.destroy()
    }

}