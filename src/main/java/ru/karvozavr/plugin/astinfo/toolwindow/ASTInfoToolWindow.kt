package ru.karvozavr.plugin.astinfo.toolwindow

import com.intellij.openapi.wm.ToolWindow
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.ui.components.JBList
import com.intellij.ui.treeStructure.Tree
import ru.karvozavr.plugin.astinfo.ASTInfoData
import ru.karvozavr.plugin.astinfo.ASTInfoModel
import javax.swing.JPanel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ASTInfoToolWindow(private val toolWindow: ToolWindow) {

    private lateinit var content: JPanel
    private lateinit var tree: Tree
    private lateinit var infoList: JBList<String>

    private val ignoredNodeTypes = listOf<Class<out Any>>(PsiWhiteSpace::class.java)

    init {
        this.reset()
    }

    fun getToolWindowContent(): JPanel {
        return content
    }

    fun updateView(model: ASTInfoModel) {
        reset()

        val treeModel = tree.model as DefaultTreeModel
        val root = treeModel.root as DefaultMutableTreeNode

        model.psiElements.forEach { node -> updateModelWithNode(node, treeModel, root) }
        infoList.setListData(model.infoData.toArray())

        toolWindow.activate { toolWindow.show(null) }
    }

    private fun reset() {
        val model = tree.model as DefaultTreeModel
        model.setRoot(DefaultMutableTreeNode("Selection"))
        infoList.setListData(ASTInfoData(0, 0, 0).toArray())
    }

    private fun updateModelWithNode(element: PsiElement, model: DefaultTreeModel, parent: DefaultMutableTreeNode) {
        val node = DefaultMutableTreeNode(element.toString())

        if (ignoredNodeTypes.none { it.isInstance(element) }) {
            model.insertNodeInto(node, parent, parent.childCount)
        }

        element.children.forEach { updateModelWithNode(it, model, node) }
    }
}
