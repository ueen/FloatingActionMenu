package de.ueen.fabmenu

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import java.util.*

class FloatingActionMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), CoordinatorLayout.AttachedBehavior  {

    private var extruded = false
    var extrudeDuration = 100L
    private var onActionClickListener: OnActionClickListener? = null
    private var selectable: Boolean = false
    private val extrudeColor: Int
    private val fab: FloatingActionButton
    private val tags = mutableListOf<ActionItem>()

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, defStyleAttr, 0);

        extrudeColor = attributes.getColor(R.styleable.FloatingActionMenu_extrudeColor,
            ContextCompat.getColor(context, R.color.design_default_color_secondary_variant))

        //linearlayout setup
        orientation = HORIZONTAL

        clipChildren = false
        clipToPadding = false

        if (attributes.getBoolean(R.styleable.FloatingActionMenu_useCompatPadding, true)) {
            val dpV = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                18f,
                context.resources.displayMetrics
            ).toInt()
            val dpH = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                context.resources.displayMetrics
            ).toInt()
            setPadding(dpH/2, dpV/2, dpH, dpV)
        }

        background = InsetDrawable(
            MaterialShapeDrawable(
                ShapeAppearanceModel.Builder()
                    .setAllCorners(CornerFamily.ROUNDED, 100f)
                    .build()
                ).apply { setTint(extrudeColor) },
            paddingLeft, paddingTop, paddingRight, paddingBottom)

        //add main FAB
        fab = FloatingActionButton(context)
        //fab.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.getDrawable(R.styleable.FloatingActionMenu_icon)?.let { icon ->
            fab.setImageDrawable(icon)
        }
        setFabColor(
            attributes.getColor(R.styleable.FloatingActionMenu_color,
                ContextCompat.getColor(context, R.color.design_default_color_secondary))
        )
        fab.setOnClickListener {
            if (!extruded) {
                extrude()
            } else {
                collapse()
            }
        }
        addView(fab)

        attributes.recycle()
    }

    fun extrude() {
        if (!extruded) {
            transition(View.VISIBLE)
        }
    }

    fun collapse(icon: Int? = null) {
        if (extruded) {
            transition(View.GONE, icon)
        }
    }

    private fun transition(visibility: Int, icon: Int? = null) {
        extruded = !extruded

        TransitionManager.beginDelayedTransition(
            this,
            AutoTransition().apply {
                setDuration(extrudeDuration)
                addListener(object : Transition.TransitionListener {
                    override fun onTransitionStart(transition: Transition) {}
                    override fun onTransitionEnd(transition: Transition) {
                        icon?.let { setFabIcon(it) }
                    }
                    override fun onTransitionCancel(transition: Transition) {}
                    override fun onTransitionPause(transition: Transition) {}
                    override fun onTransitionResume(transition: Transition) {}
                })
            }
        )

        children.forEach { v ->
            if (v is MaterialButton) {
                v.visibility = visibility
            }
        }
    }

    fun addActions(vararg actionItems: ActionItem) {
        actionItems.forEach { item -> addAction(item) }
    }

    fun addAction(actionItem: ActionItem) {
        val materialButton = MaterialButton(context, null, R.style.Widget_MaterialComponents_Button_UnelevatedButton)
        materialButton.insetTop = 0
        materialButton.insetBottom = 0
        materialButton.cornerRadius = 100
        materialButton.icon = ContextCompat.getDrawable(context, actionItem.icon)
        materialButton.iconPadding = 0
        materialButton.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
        materialButton.setBackgroundColor(extrudeColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            actionItem.tooltip?.let { tip ->
                materialButton.tooltipText = tip
            }
        }
        materialButton.setOnClickListener {
            onActionClickListener?.onActionClick(actionItem)
            collapse(if (selectable) actionItem.icon else null)
        }

        val index = childCount - 1
        addView(materialButton, index)

        materialButton.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        materialButton.post {
            materialButton.width = materialButton.height
            materialButton.visibility = View.GONE
        }

        tags.add(index, actionItem)
    }

    fun removeAction(index: Int) {
        removeViewAt(index)
        tags.removeAt(index)
    }

    fun removeAction(tag: String) {
        removeAction(tagToIndex(tag))
    }

    class ActionItem(val icon: Int, val tag: String = UUID.randomUUID().toString(), val tooltip: String? = null)

    fun setOnActionClickListener(selectable: Boolean = false, l: (actionItem: ActionItem) -> Unit) {
        this.selectable = selectable
        onActionClickListener = object: OnActionClickListener {
            override fun onActionClick(actionItem: ActionItem) {
                l(actionItem)
            }
        }
    }

    fun setOnActionClickListener(selectable: Boolean = false, l: OnActionClickListener) {
        this.selectable = selectable
        onActionClickListener = l
    }

    interface OnActionClickListener {
        fun onActionClick(actionItem: ActionItem)
    }

    fun setFabIcon(icon: Int) {
        fab.setImageDrawable(ContextCompat.getDrawable(context, icon))
    }

    fun setFabColor(color: Int) {
        fab.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun selectActionItemToFab(tag: String) {
        selectActionItemToFab(tagToIndex(tag))
    }

    fun selectActionItemToFab(index: Int) {
        tags.getOrNull(index)?.let { ai ->
            setFabIcon(ai.icon)
        }
    }

    fun tagToIndex(tag: String): Int {
        return tags.indexOfFirst { it.tag == tag }
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return object : HideBottomViewOnScrollBehavior<FloatingActionMenu>() {
            override fun slideDown(child: FloatingActionMenu) {
                super.slideDown(child)
                child.collapse()
            }

            override fun slideUp(child: FloatingActionMenu) {
                super.slideUp(child)
                child.collapse()
            }
        }
    }

}