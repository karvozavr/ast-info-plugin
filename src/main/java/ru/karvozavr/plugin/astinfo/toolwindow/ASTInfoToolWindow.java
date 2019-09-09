package ru.karvozavr.plugin.astinfo.toolwindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.intellij.ui.treeStructure.Tree;
import ru.karvozavr.plugin.astinfo.ASTInfoData;
import ru.karvozavr.plugin.astinfo.ASTInfoModel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ASTInfoToolWindow {
    private JPanel content;
    private Tree tree;
    private JBList<String> infoList;
    private ToolWindow toolWindow;

    public ASTInfoToolWindow(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        this.reset();
    }

    public JPanel getContent() {
        return content;
    }

    public void updateView(ASTInfoModel model) {
        reset();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        model.getPsiElements().forEach(node -> updateModelWithNode(node, treeModel, root));
        infoList.setListData(model.getInfoData().toArray());
        toolWindow.activate(() -> toolWindow.show(null));
    }

    private void reset() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.setRoot(new DefaultMutableTreeNode("Selection"));
        infoList.setListData(new ASTInfoData(0, 0, 0).toArray());
    }

    private void updateModelWithNode(PsiElement element, DefaultTreeModel model, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(element.toString());
        model.insertNodeInto(node, parent, parent.getChildCount());
        for (PsiElement child : element.getChildren()) {
            updateModelWithNode(child, model, node);
        }
    }
}
