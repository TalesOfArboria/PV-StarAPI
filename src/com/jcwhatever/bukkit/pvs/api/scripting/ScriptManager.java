/* This file is part of PV-StarAPI for Bukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) JCThePants (www.jcwhatever.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package com.jcwhatever.bukkit.pvs.api.scripting;

import com.jcwhatever.bukkit.generic.scripting.api.IScriptApi;
import com.jcwhatever.bukkit.pvs.api.events.AbstractArenaEvent;
import com.jcwhatever.bukkit.pvs.api.modules.PVStarModule;

import javax.annotation.Nullable;
import javax.script.ScriptEngineManager;
import java.util.List;

/**
 * Manages unevaluated scripts which can
 * be added to arenas via their {@code ArenaScriptManager}.
 */
public interface ScriptManager {

    /**
     * Get the script engine manager that provides script engines.
     */
    ScriptEngineManager getEngineManager();

    /**
     * Register an arena event class to make it available to scripts.
     *
     * @param eventClass  The owning module.
     * @param eventClass  The event class to register.
     */
    void registerEventType(PVStarModule module, Class<? extends AbstractArenaEvent> eventClass);

    /**
     * Get a registered event type.
     *
     * @param name  The name of the event type. The name is the name of the registering module
     *              followed by a period and the name of the event class. If the event is a PV-Star
     *              event, then the name is simply the event class name.
     */
    @Nullable
    Class<? extends AbstractArenaEvent> getEventType(String name);

    /**
     * Add a script.
     *
     * @param script  The script to add.
     */
    void addScript(Script script);

    /**
     * Remove a script.
     *
     * @param scriptName  The name of the script to remove.
     */
    void removeScript(String scriptName);

    /**
     * Register an api type to be included with all
     * evaluated scripts.
     *
     * @param api  The api
     */
    void registerApiType(IScriptApi api);

    /**
     * Get a script api by variable name.
     *
     * @param apiVariableName  The variable name used by the script api
     */
    @Nullable
    IScriptApi getScriptApi(String apiVariableName);

    /**
     * Get all script api.
     * @return
     */
    List<IScriptApi> getScriptApis();

    /**
     * Get a script by name.
     *
     * @param scriptName  The name of the script. (the relative path using dots instead of dashes)
     */
    @Nullable
    Script getScript(String scriptName);

    /**
     * Get the names of all scripts.
     */
    List<String> getScriptNames();

    /**
     * Get all scripts.
     */
    List<Script> getScripts();

    /**
     * Reload scripts.
     */
    void reload();
}
