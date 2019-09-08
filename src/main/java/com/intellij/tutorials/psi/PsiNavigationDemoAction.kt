/*
package com.intellij.tutorials.psi

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil

class PsiNavigationDemoAction : AnAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val editor = anActionEvent.getData(CommonDataKeys.EDITOR)
        val psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE)

        if (editor == null || psiFile == null) return
        val offset = editor.caretModel.offset

        val beginElement: PsiElement? = psiFile.findElementAt(editor.selectionModel.selectionStart)
        val endElement: PsiElement? = psiFile.findElementAt(editor.selectionModel.selectionEnd)

        val infoBuilder = StringBuilder()
        val element = psiFile.findElementAt(offset)
        infoBuilder.append("Element at caret: ").append(element).append("\n")

        if (beginElement != null && endElement != null) {
            infoBuilder.append("Selection: ${beginElement.text} ${endElement.text}\n")
            val lca = PsiTreeUtil.findCommonContext(beginElement, endElement)
            infoBuilder.append("Common Context: ${lca}\n\n\n")
            PsiTreeUtil.processElements(lca) {
                infoBuilder.append("$it\n")
                true
            }
        }

        if (element != null) {
            val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)

            infoBuilder
                    .append("Containing method: ")
                    .append(containingMethod?.name ?: "none")
                    .append("\n")
            if (containingMethod != null) {
                val containingClass = containingMethod.containingClass
                infoBuilder
                        .append("Containing class: ")
                        .append(if (containingClass != null) containingClass.name else "none")
                        .append("\n")

                infoBuilder.append("Local variables:\n")
                containingMethod.accept(object : JavaRecursiveElementVisitor() {
                    override fun visitLocalVariable(variable: PsiLocalVariable) {
                        super.visitLocalVariable(variable)
                        infoBuilder.append(variable.name).append("\n")
                    }
                })
            }
        }
        Messages.showMessageDialog(anActionEvent.project, infoBuilder.toString(), "PSI Info", null)
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabled = editor != null && psiFile != null
    }
}
*/
