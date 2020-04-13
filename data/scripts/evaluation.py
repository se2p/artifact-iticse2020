import csv
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import glob
from itertools import combinations

from pandas import DataFrame
from tabulate import tabulate
from sklearn.metrics import matthews_corrcoef
from pylatex import Tabular, NoEscape
import numpy as np
from matplotlib.ticker import (MultipleLocator, FormatStrFormatter,
                               AutoMinorLocator)

path = '../data/results'
all_files = glob.glob(path + "/*.csv")


def read_all():
    no_remix_file = "../data/results/all_projects.csv"
    df = pd.read_csv(no_remix_file, index_col=None, header=0)
    df = df.drop_duplicates(subset=None, keep='last', inplace=False)

    return df


def read_no_remix():
    no_remix_file = "../data/results/no_remix.csv"
    df = pd.read_csv(no_remix_file, index_col=None, header=0)
    df = df.drop_duplicates(subset=None, keep='last', inplace=False)

    return df


def eval_all():
    print("All projects")
    all_projects = read_all()
    print("Number of projects in total ", all_projects["project"].count())

    check_custom_blocks(all_projects)
    check_pen_blocks(all_projects)
    summary(all_projects)
    print()


def eval_without_remixes():
    print("Projects excluding remixes")
    no_remixes = read_no_remix()
    print("Number of projects excluding remixes: ", no_remixes["project"].count())

    check_custom_blocks(no_remixes)
    check_pen_blocks(no_remixes)
    summary(no_remixes)
    print()


def check_custom_blocks(no_remixes: DataFrame):
    num_proj_with_cb = no_remixes[no_remixes["procedure_count"] > 0]["procedure_count"].count()
    print("Number of projects with custom blocks:", num_proj_with_cb)

    cb_only_df = no_remixes[["ambiguous_custom_block_signature",
                             "ambiguous_parameter_name",
                             "custom_block_with_termination",
                             "custom_block_with_forever",
                             "parameter_out_of_scope",
                             "endless_recursion", "orphaned_parameter",
                             "call_without_definition"]]
    result = cb_only_df[(cb_only_df > 0).any(axis=1)]
    print("Number of projects with custom black based bug patterns: ", result.count()[0])


def check_pen_blocks(no_remixes: DataFrame):
    using_pen = no_remixes[no_remixes["using_pen"] > 0]["using_pen"].count()
    print("Number of projects using pen blocks: ", using_pen)
    pen_only_df = no_remixes[["missing_erase_all", "missing_pen_up", "missing_pen_down"]]
    result = pen_only_df[(pen_only_df > 0).any(axis=1)]
    print("Number of projects with pen based bug patterns", result.count()[0])


def summary(df: DataFrame):
    data_only = drop_info_columns(df)
    summed_df = instance_count(data_only)
    summed_bdf = binary_count(data_only, df)
    print_summary(summed_df, summed_bdf, df)


def instance_count(data_only: DataFrame):
    summed_up = data_only.sum()
    summed_df = pd.DataFrame(summed_up)
    summed_df = summed_df.reset_index()
    summed_df = summed_df.rename(columns={"index": "Bugpattern", 0: "Count"})
    summed_df["Bugpattern"] = summed_df["Bugpattern"].apply(get_pretty_name)

    return summed_df


def binary_count(data_only: DataFrame, df: DataFrame):
    binary_df = data_only.where(df == 0, 1)
    summed_binary = binary_df.sum()
    summed_bdf = pd.DataFrame(summed_binary)
    summed_bdf = summed_bdf.reset_index()
    summed_bdf = summed_bdf.rename(columns={"index": "Bugpattern", 0: "Count"})

    return summed_bdf


def print_summary(summed_df, summed_bdf, df):
    averages = []
    for pattern in summed_bdf["Bugpattern"]:
        # weighted_method_counts for projects where this pattern occurs
        wmc_series = df[df[pattern] > 0]["weighted_method_count"]
        avg_wmc = wmc_series.sum() / len(wmc_series)
        averages.append(round(avg_wmc, 2))

    summed_bdf["AVG_WMC"] = averages
    summed_bdf.sort_values(by="Bugpattern")

    # Number of projects with at least one bug
    print()
    print("Buggy Projects", summed_bdf["Count"].sum())
    print("Bugs in total", summed_df["Count"].sum())

    summed_df = summed_df.sort_values(by="Bugpattern")
    print()
    print("How many bug pattern instances occur across projects")
    print(tabulate(summed_df, headers='keys', showindex=False))

    summed_bdf["Bugpattern"] = summed_bdf["Bugpattern"].apply(get_pretty_name)
    print()
    print("How many projects have each pattern")
    print(tabulate(summed_bdf, headers='keys', showindex=False))


def get_pretty_name(pattern_name):
    pretty_name = pattern_name.replace("_", " ")
    pretty_name = pretty_name.title()

    if pretty_name == "Never Sent Message":
        return "Message Never Sent"
    elif pretty_name == "Never Receive Message":
        return "Message Never Received"
    elif pretty_name == "Ambiguous Custom Block Signature":
        return "Ambiguous CB Signature"
    elif pretty_name == "Expression as Color":
        return "Expression As T. or C. "

    return pretty_name


def drop_info_columns(df: DataFrame):
    data_only = df.drop(['project'], axis=1)
    data_only = data_only.drop(['procedure_count'], axis=1)
    data_only = data_only.drop(['block_count'], axis=1)
    data_only = data_only.drop(['sprite_count'], axis=1)
    data_only = data_only.drop(['weighted_method_count'], axis=1)
    data_only = data_only.drop(['using_pen'], axis=1)

    return data_only


if __name__ == "__main__":
    eval_all()
    eval_without_remixes()
