package io.neoterm.utils

import android.content.Context
import io.neoterm.R
import io.neoterm.backend.TerminalSession
import io.neoterm.component.font.FontComponent
import io.neoterm.frontend.component.ComponentManager
import io.neoterm.frontend.preference.NeoPreference
import io.neoterm.frontend.shell.ShellParameter
import io.neoterm.frontend.shell.ShellTermSession
import io.neoterm.frontend.terminal.TerminalView
import io.neoterm.frontend.terminal.TerminalViewClient
import io.neoterm.frontend.terminal.eks.ExtraKeysView

/**
 * @author kiva
 */
object TerminalUtils {
    fun setupTerminalView(terminalView: TerminalView?, terminalViewClient: TerminalViewClient? = null) {
        terminalView?.textSize = NeoPreference.getFontSize();

        val fontComponent = ComponentManager.getComponent<FontComponent>()
        fontComponent.applyFont(terminalView, null, fontComponent.getCurrentFont())

        if (terminalViewClient != null) {
            terminalView?.setTerminalViewClient(terminalViewClient)
        }
    }

    fun setupExtraKeysView(extraKeysView: ExtraKeysView?) {
        val fontComponent = ComponentManager.getComponent<FontComponent>()
        val font = fontComponent.getCurrentFont()
        fontComponent.applyFont(null, extraKeysView, font)
    }

    fun setupTerminalSession(session: TerminalSession?) {
    }

    fun createShellSession(context: Context, parameter: ShellParameter): TerminalSession {
        val initCommand = parameter.initialCommand ?:
                NeoPreference.loadString(R.string.key_general_initial_command, "")

        val session = ShellTermSession.Builder()
                .executablePath(parameter.executablePath)
                .currentWorkingDirectory(parameter.cwd)
                .callback(parameter.sessionCallback)
                .systemShell(parameter.systemShell)
                .envArray(parameter.env)
                .argArray(parameter.arguments)
                .create(context)
        setupTerminalSession(session)
        session.initialCommand = initCommand
        return session
    }

    fun escapeString(s: String?): String {
        if (s == null) {
            return ""
        }

        val builder = StringBuilder()
        val specialChars = "\"\\$`!"
        builder.append('"')
        val length = s.length
        for (i in 0..length - 1) {
            val c = s[i]
            if (specialChars.indexOf(c) >= 0) {
                builder.append('\\')
            }
            builder.append(c)
        }
        builder.append('"')
        return builder.toString()
    }
}