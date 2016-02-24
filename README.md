# About

This is a mod implementing the TechnicSolder api in minecraft itself. It uses an embedded Grizzly Web Server and Jersey (JAX-RS).

# First Usage

 * Download the latest version of the mod and put it in your mods/ dir
 * Launch server and stop it after it finished
 * Delete soldercache/\*.txt , soldercache/mods/\* and soldercache/modpack/\*.json
 * Edit config/minecraftsolder.cfg

You're done.

#Updating

 * Stop the server
 * Update relevant mods in /mods
 * Update relevant mods in /soldercache/clientmods
 * (Optional) You can start up the server and investigate eventual issues. After you've done, stop it.
 * Update version in config/minecraftsolder.cfg
 * Start up the server. The cache will be updated.

