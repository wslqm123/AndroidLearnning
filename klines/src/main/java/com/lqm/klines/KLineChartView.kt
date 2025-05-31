package com.lqm.klines

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import com.lqm.klines.model.KLineChartData
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Composable
fun KLineChartView(kLineChartData: KLineChartData, loadMore: (forward: Boolean) -> Unit) {

    Log.d("KLineChartView", "recomposition")

    var dragOffsetX by remember { mutableStateOf(0) }
    var currentScale by remember { mutableStateOf(1f) }
    var dragOffsetSize by remember { mutableStateOf(kLineChartData.offsetSize) }
    val visibleItemSize by remember(currentScale) {
        derivedStateOf {
            kLineChartData.maxVisibleItems / currentScale
        }
    }
    val visibleStartIndex by remember(
        kLineChartData.data, dragOffsetSize, visibleItemSize
    ) {
        derivedStateOf {
            max(
                (kLineChartData.data.size - dragOffsetSize - visibleItemSize).toInt(),
                0
            )
        }
    }
    val visibleEndIndex by remember(
        kLineChartData.data, dragOffsetSize
    ) {
        derivedStateOf {
            min(
                kLineChartData.data.size,
                kLineChartData.data.size - floor(dragOffsetSize).toInt()
            )
        }
    }

    val visibleKLineItems by remember(visibleStartIndex, visibleEndIndex) {
        derivedStateOf {
            if (visibleStartIndex >= visibleEndIndex) {
                emptyList()
            } else {
                Log.d(
                    "KLineChartView", "visible item from: $visibleStartIndex to $visibleEndIndex"
                )
                kLineChartData.data.subList(visibleStartIndex, visibleEndIndex)
            }
        }
    }

    val currentDisplayHighPrice by remember(visibleKLineItems) {
        derivedStateOf {
            visibleKLineItems.maxOfOrNull { it.high } ?: 1.0
        }
    }

    val currentDisplayLowPrice by remember(visibleKLineItems) {
        derivedStateOf {
            visibleKLineItems.minOfOrNull { it.low } ?: 0.0
        }
    }

    var kLineWidthPx by remember { mutableStateOf(10f) }
    val kLineScaleWidthPx by remember(kLineWidthPx, currentScale) {
        derivedStateOf {
            kLineWidthPx * currentScale
        }
    }

    val dragLimitLeft by remember(kLineChartData.data, visibleItemSize) {
        derivedStateOf {
            -((visibleItemSize / 2 + kLineChartData.offsetSize) * kLineScaleWidthPx).toInt()
        }
    }
    val dragLimitRight by remember(kLineChartData.data, visibleItemSize) {
        derivedStateOf {
            ((kLineChartData.data.size - (visibleItemSize / 2 + kLineChartData.offsetSize)) * kLineScaleWidthPx).toInt()
        }
    }

    val draggableState = rememberDraggableState { delta ->
        // delta 是拖动的像素增量
        dragOffsetX += delta.toInt()
    }

    LaunchedEffect(visibleStartIndex, kLineChartData.hasMoreData) {
        if (visibleStartIndex == 0 && kLineChartData.hasMoreData) {
            loadMore.invoke(false)
        }
    }

    LaunchedEffect(dragOffsetX) {
        if (dragOffsetX < dragLimitLeft) {
            dragOffsetX = dragLimitLeft
        } else if (dragOffsetX > dragLimitRight) {
            dragOffsetX = dragLimitRight
        }
        dragOffsetSize = dragOffsetX / kLineScaleWidthPx + kLineChartData.offsetSize
        Log.d(
            "KLineChartView",
            "offsetX: $dragOffsetX, limit from: $dragLimitLeft to $dragLimitRight"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .onGloballyPositioned { coordinates ->
                // 获取组件的宽度（单位是像素）
                kLineWidthPx = coordinates.size.width.toFloat() / kLineChartData.maxVisibleItems
            }
            .pointerInput(Unit) {
                coroutineScope {
                    awaitPointerEventScope {
                        while (true) { //  持续监听事件
                            val event = awaitPointerEvent(PointerEventPass.Main)
                            val changes = event.changes
                            //  检查是否有任何手指按下
                            val anyPressed = changes.any { it.pressed }
                            if (!anyPressed) {
                                //  所有手指都抬起了，可以重置一些手势状态（如果需要）
                                continue
                            }
                            //  获取当前所有按下的指针
                            val activePointers = currentEvent.changes.filter { it.pressed }
                            if (activePointers.size >= 2) {
                                try {
                                    val zoom = event.calculateZoom()
                                    val centroid =
                                        event.calculateCentroid(useCurrent = true) // 获取相对于当前组件的中心点
                                    if (zoom != 1f) { //  发生了缩放
                                        val oldScale = currentScale
                                        currentScale *= zoom
                                        currentScale = currentScale.coerceIn(0.2f, 5f)

                                        //  调整 dragOffsetX 以实现围绕手势中心缩放
                                        //  计算手势中心点在 Canvas 坐标系中，在缩放前的“数据单位”位置
                                        val dataPositionAtCentroidX =
                                            (dragOffsetX + centroid.x) / (kLineWidthPx * oldScale)
                                        //  新的 dragOffsetX 使得该数据位置在新的缩放比例下，仍然在 centroid.x 处
                                        dragOffsetX =
                                            ((dataPositionAtCentroidX * (kLineWidthPx * currentScale)) - centroid.x).toInt()
                                    }
                                    changes.forEach { it.consume() }

                                } catch (e: Exception) {
                                    //  手势被中断或不符合预期
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal //  指定拖动方向
            )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()

        ) {
            val priceDensity =
                size.height / (currentDisplayHighPrice - currentDisplayLowPrice).toFloat()
            // 位移修正变量
            val overOffset =
                visibleStartIndex - (kLineChartData.data.size - dragOffsetSize - visibleItemSize)
            visibleKLineItems.forEachIndexed { index, item ->
                drawLine(
                    color = if (item.open > item.close) Color.Red else Color.Green,
                    start = Offset(
                        (overOffset + index + 0.5f) * kLineScaleWidthPx,
                        ((currentDisplayHighPrice - item.high) * priceDensity).toFloat()
                    ),
                    end = Offset(
                        (overOffset + index + 0.5f) * kLineScaleWidthPx,
                        ((currentDisplayHighPrice - item.low) * priceDensity).toFloat()
                    ),
                    strokeWidth = 2f
                )
                drawRect(
                    color = if (item.open > item.close) Color.Red else Color.Green,
                    topLeft = Offset(
                        (index + overOffset) * kLineScaleWidthPx + 1,
                        ((currentDisplayHighPrice - max(
                            item.open,
                            item.close
                        )) * priceDensity).toFloat()
                    ),
                    size = Size(
                        kLineScaleWidthPx - 2,
                        max((abs(item.open - item.close) * priceDensity).toFloat(), 2f)
                    )
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun KLineChartViewPreview() {
//    KLineChartView() //  你可能需要提供示例数据来进行预览
}