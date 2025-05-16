import { ArrowRightIcon } from "lucide-react";
import { useToast } from "../hooks/use-toast";
import { Button } from "./ui/button";

export const SeedGenerate = () => {
    const { toast } = useToast();
    const generateSeed = () => {
        const S =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        const N = 16;
        const seed = Array.from(crypto.getRandomValues(new Uint8Array(N)))
            .map((n) => S[n % S.length])
            .join("");
        navigator.clipboard
            .writeText(seed)
            .then(() => {
                toast({
                    title: "Copied Seed",
                    description: `Copied seed to clipboard: ${seed}`,
                });
            })
            .catch((err) => {
                console.error("Failed to copy seed", err);
            });
    };

    return (
        <Button onClick={generateSeed}>
            Generate and Copy Seed <ArrowRightIcon size={16} />
        </Button>
    );
};
