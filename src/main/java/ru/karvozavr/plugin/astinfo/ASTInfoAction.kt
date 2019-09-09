package ru.karvozavr.plugin.astinfo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiFile
import ru.karvozavr.plugin.astinfo.services.AstInfoService
import kotlin.math.max


class ASTInfoAction : AnAction("AST Info") {

    override fun actionPerformed(event: AnActionEvent) {
        showASTInfo(event)
    }

    private fun showASTInfo(event: AnActionEvent) {
        val editor: Editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val psiFile: PsiFile = event.getData(CommonDataKeys.PSI_FILE) ?: return

        val beginOffset = editor.selectionModel.selectionStart
        val endOffset = max(editor.selectionModel.selectionEnd - 1, beginOffset)

        val beginElement = psiFile.findElementAt(beginOffset)
        val endElement = psiFile.findElementAt(endOffset)

        val project = event.project
        if (beginElement != null && endElement != null && project != null) {
            val contextExtractorService = ServiceManager.getService(project, AstInfoService::class.java)
            ToolWindowManager.getInstance(project).getToolWindow("AST Info").show {
                contextExtractorService?.astInfoBySelection(beginElement, endElement)
            }
        } else {
            Messages.showMessageDialog("Error getting context for selection", "Error", Messages.getErrorIcon())
        }
    }
}