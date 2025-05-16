import { Plugin } from "./plugin";

export const VelocityPlugins = () => {
    const plugin =
        "velocity, advancedportals, miniplaceholders, mckotlin-velocity, clientcatcher, luckperms, libertybans, maintenance, minimotd-velocity, miniplaceholders-luckperms-expansion, mysqldriver, noticeconnect, nuvotifier, oneversionremake, papiproxybridge, placeholderapi-expansion, plasmovoice, serverpermissions, signedvelocity, spark, tab, titleannouncer, vconsolelinker, velocityresourcepacks, vlobby, votingplugin";

    const pluginsList = plugin.split(", ");

    return (
        <ul>
            {pluginsList.map((plugin) => (
                <li key={plugin}>
                    <Plugin plugin={plugin} />
                </li>
            ))}
        </ul>
    );
};
