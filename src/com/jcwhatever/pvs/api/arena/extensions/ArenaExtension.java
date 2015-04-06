/*
 * This file is part of PV-StarAPI for Bukkit, licensed under the MIT License (MIT).
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


package com.jcwhatever.pvs.api.arena.extensions;

import com.jcwhatever.nucleus.providers.storage.DataStorage;
import com.jcwhatever.nucleus.storage.DataPath;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.PreCon;
import com.jcwhatever.pvs.api.PVStarAPI;
import com.jcwhatever.pvs.api.arena.IArena;

/**
 * Abstract implementation of an Arena extension.
 *
 * <p>Each instance is used for a single arena.</p>
 */
public abstract class ArenaExtension {

    private ArenaExtensionRegistration _registration;
    private ArenaExtensionInfo _extensionInfo;
    private IArena _arena;
    private boolean _isAttached;
    private IDataNode _dataNode;
    private String _dataNodePath;


    /**
     * Get the arena the module instance was created for.
     */
    public final IArena getArena() {
        return _arena;
    }

    /**
     * Get the modules data storage node.
     */
    public final IDataNode getDataNode() {
        return _dataNode;
    }

    /**
     * Get the name of the module.
     */
    public final String getName() {
        return _extensionInfo.name();
    }

    /**
     * Get the description of the module.
     */
    public final String getDescription() {
        return _extensionInfo.description();
    }

    /**
     * Register the extension.
     *
     * <p>For use by PV-Star implementation.</p>
     *
     * <p>Can only be registered once.</p>
     *
     * @param registration  The extension registration.
     */
    public final void register(ArenaExtensionRegistration registration) {
        PreCon.notNull(registration);

        if (_registration != null)
            throw new IllegalStateException("Arena extension can only be registered once.");

        _registration = registration;
    }

    /**
     * Invoked when the extension is attached to an arena.
     */
    protected abstract void onAttach();

    /**
     * Invoked when the extension is removed from an arena.
     */
    protected abstract void onRemove();

    /**
     * Registration used to allow implementation to access protected methods.
     */
    public static class ArenaExtensionRegistration {

        /**
         * Initialize an extension.
         *
         * @param extension  The extension to initialize.
         * @param info       The extensions info.
         * @param arena      The arena to attach the extension to.
         */
        public void attach(ArenaExtension extension, ArenaExtensionInfo info, IArena arena) {
            PreCon.notNull(extension);
            PreCon.notNull(info);
            PreCon.notNull(arena);
            PreCon.isValid(extension._registration == this, "Invalid registration.");

            if (extension._isAttached)
                throw new IllegalStateException("An arena module cannot be attached more than once.");

            extension._extensionInfo = info;
            extension._arena = arena;

            extension._dataNodePath = "arenas." + arena.getId() + ".extensions." + info.name();
            extension._dataNode = DataStorage.get(PVStarAPI.getPlugin(), new DataPath(extension._dataNodePath));
            extension._dataNode.load();

            extension._isAttached = true;
            extension.onAttach();
        }

        /**
         * Remove an extension.
         *
         * @param extension  The extension to remove.
         */
        public void remove(ArenaExtension extension) {
            PreCon.isValid(extension._registration == this, "Invalid registration.");

            if (!extension._isAttached)
                throw new IllegalStateException("Extension is not attached.");

            extension._isAttached = false;
            extension.onRemove();
            DataStorage.remove(PVStarAPI.getPlugin(), new DataPath(extension._dataNodePath));
        }
    }
}

