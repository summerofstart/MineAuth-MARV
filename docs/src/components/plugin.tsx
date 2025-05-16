import Link from "@docusaurus/Link";
import useBaseUrl from "@docusaurus/useBaseUrl";
import useGlobalData from "@docusaurus/useGlobalData";

interface DocsData {
    versions: Array<{
        docs: Array<{ id: string }>;
    }>;
}

interface GlobalData {
    "docusaurus-plugin-content-docs": {
        default: DocsData;
    };
}

export const Plugin = ({ plugin }: { plugin: string }) => {
    const globalData = useGlobalData() as unknown as GlobalData;
    const pluginUrl = useBaseUrl(`/plugins/${plugin.toLowerCase()}`);

    const list =
        globalData?.["docusaurus-plugin-content-docs"]?.default?.versions?.[0]
            ?.docs || [];

    const fileExists =
        list.filter((it) => it.id === `plugins/${plugin.toLowerCase()}`)
            .length > 0;

    return (
        <div>
            {fileExists ? (
                <Link to={pluginUrl}>
                    <b>{plugin}</b>
                </Link>
            ) : (
                <span className={"opacity-60"}>{plugin}</span>
            )}
        </div>
    );
};
